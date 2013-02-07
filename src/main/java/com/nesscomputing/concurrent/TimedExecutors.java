package com.nesscomputing.concurrent;

import com.yammer.metrics.core.MetricsRegistry;

public class TimedExecutors
{
    private TimedExecutors() {}

    public static CallableWrapper createTimerWrapper(String threadPoolName, MetricsRegistry registry)
    {
        return new TimerWrapper(threadPoolName).setMetricsRegistry(registry);
    }
}
