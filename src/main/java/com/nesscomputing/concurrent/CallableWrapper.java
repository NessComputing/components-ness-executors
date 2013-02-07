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

import java.util.concurrent.Callable;

import com.google.common.base.Throwables;

public abstract class CallableWrapper
{
    public abstract <T> Callable<T> wrap(Callable<T> callable);

    public Runnable wrap(Runnable runnable)
    {
        CallableRunnable unwrapped = new CallableRunnable(runnable);
        Callable<Void> wrapped = wrap(unwrapped);
        if (wrapped == unwrapped) {
            return runnable;
        }
        return new RunnableCallable(wrapped);
    }

    private static class RunnableCallable implements Runnable
    {
        private final Callable<?> callable;

        public RunnableCallable(Callable<?> callable)
        {
            this.callable = callable;
        }

        @Override
        public void run()
        {
            try {
                callable.call();
            } catch (Exception e) {
                throw Throwables.propagate(e);
            }
        }
    }

    private static class CallableRunnable implements Callable<Void>
    {
        private final Runnable runnable;

        public CallableRunnable(Runnable runnable)
        {
            this.runnable = runnable;
        }

        @Override
        public Void call() throws Exception
        {
            runnable.run();
            return null;
        }
    }
}
