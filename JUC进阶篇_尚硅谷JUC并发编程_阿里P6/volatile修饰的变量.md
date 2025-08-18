你这个例子里，**有 `volatile` 和没有 `volatile` 的现象差不多**，是因为：

* 你的 `T-1` 线程在 `while(true)` 里不断打印 `System.out.println()`
* `System.out.println()` 内部会调用 `PrintStream`，它用到了 `synchronized`（加锁）
* **加锁会触发 Java 内存模型的 *happens-before*** 规则：

    1. 释放锁时会将线程的本地缓存刷回主内存
    2. 获取锁时会从主内存重新读取变量值
* 所以即使 `isStop` 不是 `volatile`，`T-1` 在每次 `println()` 时都会和主内存同步一次，自然可以看到 `T-2` 修改后的值

---

### 如果想验证 `volatile` 的作用

你可以去掉 `System.out.println()`，换成一个不会触发同步的空循环，例如：

```java
while (true) {
    if (isStop) {
        break;
    }
}
```

**结果：**

* **没有 `volatile`**：`T-1` 可能永远读到旧的 `false`，陷入死循环（因为值可能一直在 CPU 缓存里没刷新）
* **有 `volatile`**：`T-1` 会及时看到 `T-2` 更新后的 `true`，退出循环

---

✅ 总结：
你现在的现象是 **`println()` 隐式帮你做了可见性保证**，所以 `volatile` 看不出差别。
在真实开发中，如果需要多线程安全可见性（而且不是通过锁同步），**`volatile` 必须保留**。

---

我可以帮你写一个**去掉 println 并能清晰对比有/无 volatile 的实验版本**，你要看吗？这样你就能肉眼看到线程卡死与否的区别。
