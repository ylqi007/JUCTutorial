
CompletableFuture 相对于 Future 具有以下优势
1. 为快速创建、连接依赖和组合多个 Future 提供了大量的便利方法。
2. 提供了适合各种开发场景的回调函数，它还提供了非常全面的异常处理支持。
3. 无缝衔接和 Lambda 表达式和 Stream API。
4. 真正意义上把异步编程、函数式编程和响应式编程多种高阶编程思想集于一身，设计上更优雅。

## 创建异步任务
1. runAsync
2. supplyAsync
3. 异步任务中的线程池
4. 异步编程思想

### runAsync() 是并发还是并行执行？
* `CompletableFuture/src/main/java/com/ylqi007/createcompletablefuture/RunAsyncDemo03.java`

* 单核CPU：两个线程使用同一个core，两个线程轮流使用一个CPU core --> 并发
* 多核CPU：core1 处理 main 线程，core2 处理 ForkJoinPool.common-worker-1 线程 --> 异步任务并行执行
* 重点：作为开发者，只要清楚如何开启异步任务，CPU硬件会把异步任务合理分配给CPU上的 core 处理