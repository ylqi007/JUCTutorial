# Chap12. Fork/Join 分支合并框架
Fork/Join 可以将一个大的任务拆分成多个子任务进行并行处理，最后将子任务结果合并成最后的计算结果，并进行输出。
Fork/Join 完成两件事情
1. Fork：把一个复杂任务进行分拆，大事化小
2. Join：把分拆任务的结果进行合并




## Reference
* ForkJoinPool
* ForkJoinTask
* RecursiveTask<V>