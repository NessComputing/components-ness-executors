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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.management.ManagementFactory;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import javax.management.MBeanServer;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.yammer.metrics.guice.InstrumentationModule;

import com.nesscomputing.config.ConfigModule;
import com.nesscomputing.lifecycle.Lifecycle;
import com.nesscomputing.lifecycle.LifecycleStage;
import com.nesscomputing.lifecycle.guice.LifecycleModule;
import com.nesscomputing.scopes.threaddelegate.ThreadDelegatedScope;
import com.nesscomputing.scopes.threaddelegate.ThreadDelegatedScopeModule;

import org.junit.Test;
import org.weakref.jmx.guice.MBeanModule;

public class LifecycleThreadPoolTest
{
    @Inject
    Lifecycle lifecycle;

    @Inject
    @Named("test")
    ExecutorService service;

    @Inject(optional=true)
    @Named("delegated")
    Provider<Object> scopedProvider;

    @Test
    public void testSameThread() throws Exception
    {
        Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure()
            {
                install (ConfigModule.forTesting());
                install (new LifecycleModule());
                install (new NessThreadPoolModule("test").withDefaultMaxThreads(0).disableThreadDelegation());
            }
        }).injectMembers(this);

        lifecycle.executeTo(LifecycleStage.START_STAGE);

        final Thread currentThread = Thread.currentThread();

        Future<Boolean> future = service.submit(new Callable<Boolean>() {
            @Override
            public Boolean call()
            {
                return currentThread == Thread.currentThread();
            }
        });

        assertTrue("must be on same thread", future.get());

        lifecycle.executeTo(LifecycleStage.STOP_STAGE);
        assertTrue(service.isShutdown());
        assertTrue(service.isTerminated());
    }

    @Test
    public void testThreadPool() throws Exception
    {
        Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure()
            {
                install (new ThreadDelegatedScopeModule());
                install (ConfigModule.forTesting());
                install (new LifecycleModule());
                install (new NessThreadPoolModule("test"));

                install (new InstrumentationModule());

                bind (MBeanServer.class).toInstance(ManagementFactory.getPlatformMBeanServer());
                install (new MBeanModule());

                bind (Object.class).annotatedWith(Names.named("delegated")).toProvider(new Provider<Object>() {
                    @Override
                    public Object get()
                    {
                        return new Object();
                    }
                }).in(ThreadDelegatedScope.SCOPE);
            }
        }).injectMembers(this);

        lifecycle.executeTo(LifecycleStage.START_STAGE);

        final Thread currentThread = Thread.currentThread();

        final Object threadDelegatedObject = scopedProvider.get();

        Future<Boolean> future = service.submit(new Callable<Boolean>() {
            @Override
            public Boolean call()
            {
                assertTrue(threadDelegatedObject == scopedProvider.get());
                return currentThread == Thread.currentThread();
            }
        });

        assertFalse("must not be on same thread", future.get());

        lifecycle.executeTo(LifecycleStage.STOP_STAGE);
        assertTrue(service.isShutdown());
        assertTrue(service.isTerminated());
    }
}
