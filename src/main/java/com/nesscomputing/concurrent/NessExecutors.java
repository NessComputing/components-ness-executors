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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Helper methods for ExecutorServices.
 */
public class NessExecutors
{

    private NessExecutors() { }

    /**
     * Wrap an {@link ExecutorService} to make it appropriate for use in a
     * try-with-resources block.  The service is shutdown but may not be terminated
     * by the end of the try statement.
     */
    public static ShutdownExecutorService autoShutdown(ExecutorService service)
    {
        return new ShutdownExecutorService(service);
    }

    /**
     * Wrap an {@link ExecutorService} to make it appropriate for use in a
     * try-with-resources block.  The service is terminated at the end of the
     * try statement unless the timeout elapses, in which case a TimeoutException is thrown.
     */
    public static TerminatingExecutorService autoTerminate(ExecutorService service, int timeout, TimeUnit units)
    {
        return new TerminatingExecutorService(service, timeout, units);
    }

    /**
     * Wrap a {@link ScheduledExecutorService} to make it appropriate for use in a
     * try-with-resources block.  The service is shutdown but may not be terminated
     * by the end of the try statement.
     */
    public static ShutdownScheduledExecutorService autoShutdown(ScheduledExecutorService service)
    {
        return new ShutdownScheduledExecutorService(service);
    }

    /**
     * Wrap a {@link ScheduledExecutorService} to make it appropriate for use in a
     * try-with-resources block.  The service is terminated at the end of the
     * try statement unless the timeout elapses, in which case a TimeoutException is thrown.
     */
    public static TerminatingScheduledExecutorService autoTerminate(ScheduledExecutorService service, int timeout, TimeUnit units)
    {
        return new TerminatingScheduledExecutorService(service, timeout, units);
    }
}
