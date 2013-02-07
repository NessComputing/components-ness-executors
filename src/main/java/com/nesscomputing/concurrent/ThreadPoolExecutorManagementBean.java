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

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.skife.config.TimeSpan;
import org.weakref.jmx.Managed;

public class ThreadPoolExecutorManagementBean implements ExecutorServiceManagementBean
{
    private final ThreadPoolExecutor service;

    ThreadPoolExecutorManagementBean(ThreadPoolExecutor service)
    {
        this.service = service;
    }

    @Override
    @Managed
    public boolean isShutdown()
    {
        return service.isShutdown();
    }

    @Override
    @Managed
    public boolean isTerminated()
    {
        return service.isTerminated();
    }

    @Override
    @Managed
    public boolean isTerminating()
    {
        return service.isTerminating();
    }

    @Override
    @Managed
    public String getRejectedExecutionHandler()
    {
        return service.getRejectedExecutionHandler().toString();
    }

    @Override
    @Managed
    public int getCorePoolSize()
    {
        return service.getCorePoolSize();
    }

    @Override
    @Managed
    public void setCorePoolSize(int corePoolSize)
    {
        service.setCorePoolSize(corePoolSize);
    }

    @Override
    @Managed
    public int getMaximumPoolSize()
    {
        return service.getMaximumPoolSize();
    }

    @Override
    @Managed
    public void setMaximumPoolSize(int maximumPoolSize)
    {
        service.setMaximumPoolSize(maximumPoolSize);
    }

    @Override
    @Managed
    public long getKeepAliveTime()
    {
        return service.getKeepAliveTime(TimeUnit.MILLISECONDS);
    }

    @Override
    @Managed
    public void setKeepAliveTime(String keepAliveTime)
    {
        setKeepAliveTime(new TimeSpan(keepAliveTime).getMillis());
    }

    @Override
    @Managed
    public void setKeepAliveTime(long keepAliveTimeMs)
    {
        service.setKeepAliveTime(keepAliveTimeMs, TimeUnit.MILLISECONDS);
    }

    @Override
    @Managed
    public int getQueueCurrentSize()
    {
        return service.getQueue().size();
    }

    @Override
    @Managed
    public int getQueueRemainingSize()
    {
        return service.getQueue().remainingCapacity();
    }

    @Override
    @Managed
    public int getCurrentPoolSize()
    {
        return service.getPoolSize();
    }

    @Override
    @Managed
    public int getCurrentlyActiveThreads()
    {
        return service.getActiveCount();
    }

    @Override
    @Managed
    public int getLargestPoolSize()
    {
        return service.getLargestPoolSize();
    }

    @Override
    @Managed
    public long getEnqueuedTaskCount()
    {
        return service.getTaskCount();
    }

    @Override
    @Managed
    public long getCompletedTaskCount()
    {
        return service.getCompletedTaskCount();
    }
}
