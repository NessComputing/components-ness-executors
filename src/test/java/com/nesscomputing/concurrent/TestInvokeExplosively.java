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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.SettableFuture;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestInvokeExplosively
{
    private ExecutorService executor;

    @Before
    public void setUp() throws Exception
    {
        executor = Executors.newCachedThreadPool();
    }

    @After
    public void tearDown() throws Exception
    {
        executor.shutdownNow();
    }

    @Test
    public void testInvokeExplosiveSuccess() throws Exception
    {
        final SettableFuture<String> f1 = SettableFuture.create();
        final SettableFuture<String> f2 = SettableFuture.create();
        final SettableFuture<String> f3 = SettableFuture.create();

        Callable<String> runner = new Callable<String>() {
            @Override
            public String call() throws Exception
            {
                Thread.sleep(100);
                f1.set("a");
                Thread.sleep(100);
                f2.set("b");
                Thread.sleep(100);
                f3.set("c");
                Thread.sleep(100);
                return "d";
            }
        };

        List<Future<String>> result = NessExecutors.invokeAllExplosively(executor, tasksFor(runner, f1, f2, f3));

        List<String> results = Lists.newArrayList();
        for (Future<String> f : result) {
            results.add(f.get());
        }

        assertEquals(ImmutableList.of("a", "b", "c", "d"), results);
    }

    @Test
    public void testInvokeExplosiveness() throws Exception
    {
        final Exception exc = new IllegalStateException();
        final SettableFuture<String> f1 = SettableFuture.create();
        final SettableFuture<String> f2 = SettableFuture.create();
        final SettableFuture<String> f3 = SettableFuture.create();

        Callable<String> runner = new Callable<String>() {
            @Override
            public String call() throws Exception
            {
                Thread.sleep(100);
                f1.set("a");
                Thread.sleep(100);
                f2.setException(exc);
                Thread.sleep(100);
                f3.set("c");
                Thread.sleep(100);
                return "d";
            }
        };

        List<Future<String>> result = NessExecutors.invokeAllExplosively(executor, tasksFor(runner, f1, f2, f3));

        assertEquals(2, result.size());
        assertEquals("a", result.get(0).get());
        try {
            result.get(1).get();
            fail();
        } catch (ExecutionException e) {
            assertEquals(exc, e.getCause());
        }
    }

    @SafeVarargs
    private static <T> List<Callable<T>> tasksFor(Callable<T> runner, Future<T>... futures)
    {
        ImmutableList.Builder<Callable<T>> result = ImmutableList.builder();
        result.add(runner);
        for (Future<T> future : futures) {
            result.add(new FutureCallable<>(future));
        }
        return result.build();
    }

    static class FutureCallable<T> implements Callable<T>
    {
        private final Future<T> future;

        FutureCallable(Future<T> future)
        {
            this.future = future;
        }

        @Override
        public T call() throws Exception
        {
            try {
                return future.get();
            } catch (ExecutionException e) {
                if (e.getCause() instanceof Exception) {
                    throw (Exception) e.getCause();
                }
                throw new AssertionError(e);
            }
        }
    }
}
