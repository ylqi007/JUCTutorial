# JUCTutorial

视频正确食用方法：了解知识点，然后自己通过源码或者查找资料完善

做难事，必有所得

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




## 命名习惯
**基础篇**
- package chap01xxx,
- class: XxxxDemo01


## Java 代码规范
1. 阿里巴巴Java开手册：以最新版(黄山版)为准。
  * [阿里巴巴Java开发手册_202008(嵩山版).pdf](docs/%E9%98%BF%E9%87%8C%E5%B7%B4%E5%B7%B4Java%E5%BC%80%E5%8F%91%E6%89%8B%E5%86%8C_202008%28%E5%B5%A9%E5%B1%B1%E7%89%88%29.pdf)
  * ✅ [阿里巴巴Java开发手册_202202(黄山版).pdf](docs/%E9%98%BF%E9%87%8C%E5%B7%B4%E5%B7%B4Java%E5%BC%80%E5%8F%91%E6%89%8B%E5%86%8C_202202%28%E9%BB%84%E5%B1%B1%E7%89%88%29.pdf)
  * [黄山归来不看岳：《Java开发手册（黄山版）》新增 11 条规约](https://developer.aliyun.com/article/888697)
2. Google Java Style


## JDK版本
就我自己而言，还是先以 JDK17 为主。
1. 稳定性和新特性平衡
   * 稳定为先：对于安全性、长期维护有较高要求的项目，JDK17 是更稳健的选择。
   * 前瞻性体验：如果你的项目希望快速适应新技术、享受最新优化和特性，JDK21 则能提供更多可能性。
2. 根据项目需求定制选择
   * 企业级以及长期项目：优先考虑 JDK17，其长期支持策略能为项目提供持久稳定的保障。
   * 敏捷开发与实验项目：JDK21 优先的特性和优化，能够为项目带来更快的迭代和更多创新空间。

### Reference
* [从JDK8飞升到JDK17，再到未来的JDK21](https://www.zhihu.com/tardis/zm/art/585377119?source_id=1003)
* [新项目来了，JDK 17和JDK 21 该如何选择？](https://cloud.tencent.com/developer/article/2425043)
* [千呼万唤始出来 JDK 21 LTS, 久等了](https://www.cnblogs.com/bokers/p/17725530.html)
* [OpenJDK 21 升级指南](https://www.diguage.com/post/upgrade-to-openjdk21/)


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
* [小新成长之路](https://www.cnblogs.com/star95?page=1)
  * [万字长文深度解读Java线程池，硬核源码分析](https://www.cnblogs.com/star95/p/17714057.html)


* [一文搞懂JUC并发编程](https://www.cnblogs.com/ZhangHao-Study/p/16994667.html)

书
1. Java 并发编程实战
2. Java 并发编程的艺术
3. ~~图解 Java 并发编程~~
4. https://github.com/RedSpider1/concurrent
5. 