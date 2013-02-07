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

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;

import org.skife.config.Config;
import org.skife.config.DefaultNull;
import org.skife.config.TimeSpan;

/**
 * Configuration for {@link LifecycledThreadPoolModule}.
 */
interface ThreadPoolConfiguration {

    static final int DEFAULT_MIN_THREADS = 1;
    static final int DEFAULT_MAX_THREADS = 16;
    static final TimeSpan DEFAULT_TIMEOUT = new TimeSpan("30m");
    static final int DEFAULT_QUEUE_SIZE = 100;
    static final RejectedHandler DEFAULT_REJECTED_HANDLER = RejectedHandler.CALLER_RUNS;

    /**
     * Configuration options to select {@link RejectedExecutionHandler}s.
     */
    public enum RejectedHandler {
        /**
         * @see CallerRunsPolicy
         */
        CALLER_RUNS {
            @Override
            RejectedExecutionHandler getHandler() {
                return new ThreadPoolExecutor.CallerRunsPolicy();
            }
        },
        /**
         * @see AbortPolicy
         */
        ABORT {
            @Override
            RejectedExecutionHandler getHandler() {
                return new ThreadPoolExecutor.AbortPolicy();
            }
        },
        /**
         * @see DiscardPolicy
         */
        DISCARD_NEWEST {
            @Override
            RejectedExecutionHandler getHandler() {
                return new ThreadPoolExecutor.DiscardPolicy();
            }
        },
        /**
         * @see DiscardOldestPolicy
         */
        DISCARD_OLDEST {
            @Override
            RejectedExecutionHandler getHandler() {
                return new ThreadPoolExecutor.DiscardOldestPolicy();
            }
        };

        abstract RejectedExecutionHandler getHandler();
    }

    /**
     * The minimum thread pool size.  Must be {@code 0 < min < max} unless max is {@code 0}.
     */
    @Config("min-threads")
    @DefaultNull // (DEFAULT_MIN_THREADS)
    Integer getMinThreads();

    /**
     * The maximum thread pool size.  May be 0, in which case there is no thread pool.  All
     * requests would then execute directly in the calling thread, which is good for testing
     * and debugging.
     */
    @Config("max-threads")
    @DefaultNull // (DEFAULT_MAX_THREADS)
    Integer getMaxThreads();

    /**
     * How long a thread may remain totally idle before the pool shrinks.
     */
    @Config("timeout")
    @DefaultNull // (DEFAULT_TIMEOUT)
    TimeSpan getThreadTimeout();

    /**
     * The size of the {@link ArrayBlockingQueue}.  May be 0, in which case there is a {@link SynchronousQueue} instead.
     */
    @Config("queue-size")
    @DefaultNull // (DEFAULT_QUEUE_SIZE)
    Integer getQueueSize();

    /**
     * The rejected execution handler to use for the thread pool.
     * @see RejectedHandler
     */
    @Config("rejected-handler")
    @DefaultNull // (DEFAULT_REJECTED_HANDLER)
    RejectedHandler getRejectedHandler();
}
