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

import org.weakref.jmx.Managed;

public interface ExecutorServiceManagementBean
{
    @Managed boolean isShutdown();
    @Managed boolean isTerminated();
    @Managed boolean isTerminating();
    @Managed String getRejectedExecutionHandler();
    @Managed int getCorePoolSize();
    @Managed void setCorePoolSize(int corePoolSize);
    @Managed int getMaximumPoolSize();
    @Managed void setMaximumPoolSize(int maximumPoolSize);
    @Managed long getKeepAliveTime();
    @Managed void setKeepAliveTime(String keepAliveTime);
    @Managed void setKeepAliveTime(long keepAliveTimeMs);
    @Managed int getQueueCurrentSize();
    @Managed int getQueueRemainingSize();
    @Managed int getCurrentPoolSize();
    @Managed int getCurrentlyActiveThreads();
    @Managed int getLargestPoolSize();
    @Managed long getEnqueuedTaskCount();
    @Managed long getCompletedTaskCount();
}
