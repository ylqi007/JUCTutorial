# 线程间的定制化通信

## 案例
> 启动三个线程，实现如下要求
> (AA打印5次，BB打印10次，CC打印15次)x10, 即总共重复10轮

实现思路：为每个线程创建一个标志位，是该标志位则执行操作，执行完修改成下一个标志位，通知下一个线程开始执行。

* Example: `demo01/src/main/java/com/ylqi007/communication/ThreadDemo04.java`