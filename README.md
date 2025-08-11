# JUCTutorial

视频正确食用方法：了解知识点，然后自己通过源码或者查找资料完善

## 结构说明
```shell
➜  JUCTutorial git:(main) ✗ tree -L 2
.
├── AdvanceDemo01
│   ├── pom.xml
│   ├── src
│   └── target
├── CompletableFuture
│   ├── ReadMe.md
│   ├── pom.xml
│   ├── src
│   └── target
├── Draft.md
├── README.md
├── demo01
│   ├── pom.xml
│   ├── src
│   └── target
├── docs
│   ├── Chap01.JUC概述.md
│   ├── Chap02.synchronized关键字和Lock接口.md
│   ├── Chap03.线程间的通信.md
│   ├── CompletableFuture异步编程
│   ├── CompletableFuture异步编程.rar
│   ├── JUC.mmap
│   ├── others
│   ├── 尚硅谷高级技术之JUC高并发编程.pdf
│   ├── 尚硅谷高级技术之JUC高并发编程.xmind
│   └── 进程与线程的区别.md
└── pom.xml
```

## Java 代码规范
1. 阿里巴巴Java开手册：以最新版(黄山版)为准。
   * [阿里巴巴Java开发手册_202008(嵩山版).pdf](docs/%E9%98%BF%E9%87%8C%E5%B7%B4%E5%B7%B4Java%E5%BC%80%E5%8F%91%E6%89%8B%E5%86%8C_202008%28%E5%B5%A9%E5%B1%B1%E7%89%88%29.pdf)
   * ✅ [阿里巴巴Java开发手册_202202(黄山版).pdf](docs/%E9%98%BF%E9%87%8C%E5%B7%B4%E5%B7%B4Java%E5%BC%80%E5%8F%91%E6%89%8B%E5%86%8C_202202%28%E9%BB%84%E5%B1%B1%E7%89%88%29.pdf)
   * [黄山归来不看岳：《Java开发手册（黄山版）》新增 11 条规约](https://developer.aliyun.com/article/888697)
2. Google Java Style


## 命名习惯
**基础篇**
- package chap01xxx,
- class: XxxxDemo01




## Reference
* ✅B站: [【尚硅谷】大厂必备技术之JUC并发编程](https://www.bilibili.com/video/BV1Kw411Z7dF) (基础篇)
  * [【尚硅谷】大厂必备技术之JUC并发编程——笔记总结](https://blog.csdn.net/xt199711/article/details/123029986?spm=1001.2014.3001.5502)
* ✅B站: [尚硅谷JUC并发编程与源码分析（对标阿里P6-P7）](https://www.bilibili.com/video/BV1ar4y1x727/) (进阶篇)
  * 📒笔记: [语雀：JUC并发编程](https://www.yuque.com/gongxi-wssld/csm31d)
  * 脑图：[GitHub: JUC_Advance](https://github.com/hao888TUV/JUC_Advance)
  * GitHub?
* GitHub: https://github.com/shuhongfan/JUC
* https://gitee.com/bzxhh/juc_atguigu
* 读写锁那一块个人感觉讲得不好，可以看看这两篇博客，我觉得不错。https://www.jianshu.com/p/9cd5212c8841，https://segmentfault.com/a/1190000021962190
* [【多线程】锁机制详解](https://blog.csdn.net/qq_34416331/article/details/107764522)
* [2024最新!CompletableFuture异步编程详解一[入门]](https://blog.csdn.net/ManCxyster/article/details/135283796)
* [Java 并发编程 78 讲-完](https://learn.lianglianglee.com/%E4%B8%93%E6%A0%8F/Java%20%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B%2078%20%E8%AE%B2-%E5%AE%8C)