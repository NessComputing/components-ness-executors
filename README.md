Ness Computing Executors Component
==================================

Component Charter
-----------------

* Builds useful ExecutorServices that interact with the Ness platform gracefully.
* Integrates with ness-scopes to provide the ThreadDelegatedScope to Executor tasks.
* Integrates with yammer-metrics to provide monitoring of thread pools
* Integrates with ness-lifecycle to shut down thread pools on service shutdown
* Integrates with JMX to allow resizing of thread pools at runtime
* Integrates with ness-config to allow reconfiguration of thread pools without code rebuilds

----
Copyright (C) 2013 Ness Computing, Inc.
