# Performance Bottleneck Report

**Project:** Blogging Platform Backend (Spring Boot)  
**Test Type:** API Concurrency Test (Postman)  
**Date:** Feb 28, 2026

## Executive Summary
A 10-minute load test was executed using 20 virtual users with a ramp-up period of approximately 2 minutes 30 seconds. The system processed 2,502 total requests with an average throughput of 4.13 requests per second. The observed average response time was 1,991 ms, with an error rate of 0.96%.

Overall, the system remained stable under moderate concurrency but demonstrated noticeable latency spikes and inconsistent performance toward peak load periods. The results suggest the application can handle light production traffic but may struggle under higher concurrency without optimization.

## Test Configuration
- Virtual Users: 20
- Test Duration: 10 minutes
- Load Pattern: Gradual ramp-up
- Execution Window: 19:57:20 – 20:07:26 (GMT)

## Key Observations

### Response Time Behavior
Response times remained relatively low during early execution but increased progressively as concurrency rose. The average latency hovered around 2 seconds, which is acceptable for non-critical operations but high for interactive APIs.

More concerning is the variability in higher percentiles. The 95th and 99th percentile response times spiked significantly near peak load, indicating that a portion of requests experienced severe delays. This pattern suggests contention or blocking behavior in backend processing rather than consistent computational load.

### Throughput Stability
Throughput fluctuated throughout the test rather than scaling linearly with user load. While the system briefly approached higher request rates during mid-execution, throughput dropped sharply toward the end of the test window. This behavior commonly indicates resource saturation or inefficient request handling under sustained concurrency.

### Error Rate
The recorded error rate of 0.96% is low but non-negligible. Errors appearing during higher load periods typically signal timeouts, failed authentication flows, or thread/resource exhaustion. Even small error rates may compound under production-scale traffic.

## Performance Interpretation
The performance profile indicates that the application is not CPU-bound but likely constrained by blocking operations such as security filters, database calls, or thread pool limitations. The divergence between average response time and higher percentiles strongly implies queuing or lock contention during peak request bursts.

## Recommendations
- Investigate request processing pipeline, particularly authentication filters and database interactions.
- Monitor thread pool usage and connection pool limits under load.
- Optimize blocking operations or introduce asynchronous handling where beneficial.
- Re-run testing with incremental user scaling to identify saturation thresholds.

## Conclusion
The system demonstrates functional stability under moderate load but exhibits latency variability and throughput degradation as concurrency increases. Optimization efforts should focus on reducing request blocking and improving scalability before deploying to a high-traffic environment.