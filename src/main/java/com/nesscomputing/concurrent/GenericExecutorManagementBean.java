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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

public class GenericExecutorManagementBean implements ExecutorServiceManagementBean
{
    private final ExecutorService service;
    private final BlockingQueue<?> queue;

    GenericExecutorManagementBean(ExecutorService service, BlockingQueue<?> queue)
    {
        this.service = service;
        this.queue = queue;
    }

    @Override
    public boolean isShutdown()
    {
        return service.isShutdown();
    }

    @Override
    public boolean isTerminated()
    {
        return service.isTerminated();
    }

    @Override
    public boolean isTerminating()
    {
        return false;
    }

    @Override
    public String getRejectedExecutionHandler()
    {
        return "null";
    }

    @Override
    public int getCorePoolSize()
    {
        return 0;
    }

    @Override
    public void setCorePoolSize(int corePoolSize)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMaximumPoolSize()
    {
        return 0;
    }

    @Override
    public void setMaximumPoolSize(int maxPoolSize)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getKeepAliveTime()
    {
        return 0;
    }

    @Override
    public void setKeepAliveTime(String keepAliveTime)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setKeepAliveTime(long keepAliveTimeMs)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getQueueCurrentSize()
    {
        return queue.size();
    }

    @Override
    public int getQueueRemainingSize()
    {
        return queue.remainingCapacity();
    }

    @Override
    public int getCurrentPoolSize()
    {
        return 0;
    }

    @Override
    public int getCurrentlyActiveThreads()
    {
        return 0;
    }

    @Override
    public int getLargestPoolSize()
    {
        return 0;
    }

    @Override
    public long getEnqueuedTaskCount()
    {
        return 0;
    }

    @Override
    public long getCompletedTaskCount()
    {
        return 0;
    }
}
