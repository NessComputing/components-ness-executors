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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

public class TestTryWithResourcesExecutorShutdown
{
    @Test
    public void testShutdownExecutorService()
    {
        ExecutorService service = createMock(ExecutorService.class);
        service.shutdown();
        expectLastCall().once();
        replay(service);

        try (ShutdownExecutorService innerService = NessExecutors.autoShutdown(service)) {
            // do nothing
        }

        verify(service);
    }

    @Test
    public void testShutdownScheduledExecutorService()
    {
        ScheduledExecutorService service = createMock(ScheduledExecutorService.class);
        service.shutdown();
        expectLastCall().once();
        replay(service);

        try (ShutdownScheduledExecutorService innerService = NessExecutors.autoShutdown(service)) {
            // do nothing
        }

        verify(service);
    }

    @Test
    public void testTerminatingExecutorServiceSucceeds() throws Exception
    {
        ExecutorService service = createMock(ExecutorService.class);
        service.shutdown();
        expectLastCall().once();

        expect(service.awaitTermination(1, TimeUnit.MINUTES)).andReturn(true).once();

        replay(service);

        try (TerminatingExecutorService innerService = NessExecutors.autoTerminate(service, 1, TimeUnit.MINUTES)) {
            // do nothing
        }

        verify(service);
    }

    @Test
    public void testTerminatingExecutorServiceFails() throws Exception
    {
        ExecutorService service = createMock(ExecutorService.class);
        service.shutdown();
        expectLastCall().once();

        expect(service.awaitTermination(1, TimeUnit.MINUTES)).andReturn(false).once();

        replay(service);

        try {
            try (TerminatingExecutorService innerService = NessExecutors.autoTerminate(service, 1, TimeUnit.MINUTES)) {
                // do nothing
            }
            fail();
        } catch (TimeoutException e)
        {
            assertNotNull(e.getMessage());
        }

        verify(service);
    }

    @Test
    public void testTerminatingScheduledExecutorServiceSucceeds() throws Exception
    {
        ScheduledExecutorService service = createMock(ScheduledExecutorService.class);
        service.shutdown();
        expectLastCall().once();

        expect(service.awaitTermination(1, TimeUnit.MINUTES)).andReturn(true).once();

        replay(service);

        try (TerminatingScheduledExecutorService innerService = NessExecutors.autoTerminate(service, 1, TimeUnit.MINUTES)) {
            // do nothing
        }

        verify(service);
    }

    @Test
    public void testTerminatingScheduledExecutorServiceFails() throws Exception
    {
        ScheduledExecutorService service = createMock(ScheduledExecutorService.class);
        service.shutdown();
        expectLastCall().once();

        expect(service.awaitTermination(1, TimeUnit.MINUTES)).andReturn(false).once();

        replay(service);

        try {
            try (TerminatingScheduledExecutorService innerService = NessExecutors.autoTerminate(service, 1, TimeUnit.MINUTES)) {
                // do nothing
            }
            fail();
        } catch (TimeoutException e)
        {
            assertNotNull(e.getMessage());
        }

        verify(service);
    }
}
