/**
 * Copyright (C) 2012 Ness Computing, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nesscomputing.concurrent;

import static com.nesscomputing.scopes.threaddelegate.ThreadDelegatedScope.SCOPE;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import com.nesscomputing.scopes.threaddelegate.ThreadDelegatedContext;

public final class ThreadDelegatingDecorator
{
    public static final CallableWrapper THREAD_DELEGATING_WRAPPER = new ThreadDelegatingCallableWrapper();

    private ThreadDelegatingDecorator()
    {
    }

    public static Executor wrapExecutor(final Executor wrappedExecutor)
    {
        return DecoratingExecutors.decorate(wrappedExecutor, THREAD_DELEGATING_WRAPPER);
    }

    public static ExecutorService wrapExecutorService(final ExecutorService wrappedExecutorService)
    {
        return DecoratingExecutors.decorate(wrappedExecutorService, THREAD_DELEGATING_WRAPPER);
    }

    private static class ThreadDelegatingCallableWrapper extends CallableWrapper
    {
        @Override
        public <T> Callable<T> wrap(Callable<T> callable) {
            return new DelegatingCallable<T>(callable);
        }

        @Override
        public Runnable wrap(Runnable runnable)
        {
            return new DelegatingRunnable(runnable);
        }
    }

    private static class DelegatingRunnable implements Runnable
    {
        private final Runnable wrappedRunnable;
        private final ThreadDelegatedContext callerContext;

        DelegatingRunnable(final Runnable wrappedRunnable)
        {
            this.wrappedRunnable = wrappedRunnable;
            this.callerContext = SCOPE.getContext();
        }

        @Override
        public void run()
        {
            final ThreadDelegatedContext originalContext = SCOPE.getContext();
            try {
                // Assign the caller context.
                SCOPE.changeScope(callerContext);

                wrappedRunnable.run();
            }
            finally {
                // Reassign the original context.
                SCOPE.changeScope(originalContext);
            }
        }
    }

    private static class DelegatingCallable<C> implements Callable<C>
    {
        private final Callable<C> wrappedCallable;
        private final ThreadDelegatedContext callerContext;

        DelegatingCallable(final Callable<C> wrappedCallable)
        {
            this.wrappedCallable = wrappedCallable;
            this.callerContext = SCOPE.getContext();
        }

        @Override
        public C call() throws Exception
        {
            final ThreadDelegatedContext originalContext = SCOPE.getContext();
            try {
                // Assign the caller context.
                SCOPE.changeScope(callerContext);

                return wrappedCallable.call();
            }
            finally {
                // Tell the current context, that we are leaving.
                SCOPE.changeScope(originalContext);
            }
        }
    }
}
