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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.nesscomputing.logging.Log;

/**
 * AutoCloseable ExecutorService implementation.  The service will be terminated
 * when close returns, or it may throw TimeoutException if the timeout elapses.
 */
public class TerminatingExecutorService extends DelegatingExecutorService implements AutoCloseable
{
    private static final Log LOG = Log.findLog();
    private final int timeout;
    private final TimeUnit units;

    public TerminatingExecutorService(ExecutorService service, int timeout, TimeUnit units)
    {
        super(service);
        this.timeout = timeout;
        this.units = units;
    }

    @Override
    public void close() throws TimeoutException
    {
        ExecutorService delegate = getDelegate();
        delegate.shutdown();
        try {
            if (!delegate.awaitTermination(timeout, units)) {
                throw new TimeoutException("Executor service " + delegate + " did not shutdown after " + timeout + " " + units);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOG.debug(e, "While awaiting executor service termination");
        }
    }
}
