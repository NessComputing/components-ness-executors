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

import java.util.Set;
import java.util.concurrent.Callable;

import com.google.common.collect.ImmutableSet;

public class CallableWrappers
{

    private CallableWrappers() {}

    public static CallableWrapper combine(Iterable<CallableWrapper> wrappers)
    {
        return new CombinedCallableWrapper(wrappers);
    }

    private static class CombinedCallableWrapper extends CallableWrapper
    {
        private final Set<CallableWrapper> wrappers;

        public CombinedCallableWrapper(Iterable<CallableWrapper> wrappers)
        {
            this.wrappers = ImmutableSet.copyOf(wrappers);
        }

        @Override
        public <T> Callable<T> wrap(Callable<T> callable)
        {
            for (CallableWrapper w : wrappers) {
                callable = w.wrap(callable);
            }
            return callable;
        }

        @Override
        public Runnable wrap(Runnable runnable)
        {
            for (CallableWrapper w : wrappers) {
                runnable = w.wrap(runnable);
            }
            return runnable;
        }
    }
}
