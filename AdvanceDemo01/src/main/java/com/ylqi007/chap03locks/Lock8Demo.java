package com.ylqi007.chap03locks;


import com.ylqi007.utils.CommonUtils;
import org.junit.jupiter.api.Test;

/**
 * 线程 操作 资源类
 *
 * 现象描述：
 * 1 标准访问ab两个线程，请问先打印邮件还是短信？ --------先邮件，后短信  共用一个对象锁
 * 2. sendEmail钟加入暂停3秒钟，请问先打印邮件还是短信？---------先邮件，后短信  共用一个对象锁
 * 3. 添加一个普通的hello方法，请问先打印普通方法还是邮件？ --------先hello，再邮件
 * 4. 有两部手机，请问先打印邮件还是短信？ ----先短信后邮件  资源没有争抢，不是同一个对象锁
 * 5. 有两个静态同步方法，一步手机， 请问先打印邮件还是短信？---------先邮件后短信  共用一个类锁
 * 6. 有两个静态同步方法，两部手机， 请问先打印邮件还是短信？ ----------先邮件后短信 共用一个类锁
 * 7. 有一个静态同步方法 一个普通同步方法，请问先打印邮件还是短信？ ---------先短信后邮件   一个用类锁一个用对象锁
 * 8. 有一个静态同步方法，一个普通同步方法，两部手机，请问先打印邮件还是短信
 *
 * testLock01() and testLock02()
 *  一个对象有多个`synchronized`方法时，某一时刻，只要有一个线程去调用其中的一个`synchronized`方法，其他的线程都只能等待。
 *  换句话说，某一时刻，只能有唯一的一个线程去访问**这些** `synchronized` 方法
 *  锁的是当前对象`this`，被锁定后，其他的线程都不能进入到当前对象的其他`synchronized` 方法
 *
 * 3 & 4
 *  加上普通方法，发现和同步锁无关。
 *  换成两个对象后，不是同一把锁，情况立即变化
 *
 * 5 & 6
 *  static synchronized 是类锁
 *
 * 7 & 8
 *  (1) 当一个线程试图访问同步代码时，它首先必须得到锁，正常退出或抛出异常时必须释放锁。
 *  (2) 所有普通同步方法用的都是同一把锁，即实例对象本身，就是 new 出来的具体实例对象本省，亦即 this
 *  也就是说，如果一个实例对象的普通同步方法获取锁后，该实例对象的其他普通同步方法必须等待获取锁的方法释放锁之后，才能获取锁。
 *  (3) 所有的静态同步方法用的也是同一把锁，即类对象本身，就是常说的唯一模板 class
 *  具体实例对象this 和 唯一模板class，这两把锁是不同的对象，所以静态同步方法与普通同步方法是不存在竞态条件的。
 *  一旦一个静态同步方法获取锁后，其他的静态同步方法都必须等待该方法释放锁后，才能获取锁。
 *
 *
 * 三种synchronized锁的内容有一些差别：
 *  1. 对于普通同步方法，锁的是“当前实例对象”，通常指“this”
 *  2. 对于静态同步方法，锁的是“当前的Class对象”，如 Phone.class 是所以 Phone 实例的唯一模板
 *  3. 对于同步代码块，锁的是 synchronized 括号内的对象
 *
 *
 */
public class Lock8Demo {
    public static void main(String[] args) {
        Phone phone = new Phone();
        new Thread(() -> {
            phone.sendEmail();
        }, "Thread-A").start();

        CommonUtils.sleepSeconds(2);

        new Thread(() -> {
            phone.sendSMS();
        }, "Thread-B").start();

        // testLockExample04();
    }

    @Test
    public void testLock01() {
        Phone phone = new Phone();

        new Thread(() -> phone.sendEmail(), "Thread-A").start();
        new Thread(() -> phone.sendSMS(), "Thread-B").start();

        CommonUtils.sleepSeconds(1);
    }

    /**
     * Thread-A ==> synchronized sendEmailWithSleep(2s)
     * Thread-B ==> synchronized sendSMS()
     *
     * Thread-B 要等 Thread-A 结束后，才可以访问
     */
    @Test
    public void testLock02() {
        Phone phone = new Phone();

        new Thread(() -> phone.sendEmailWithSleep(2), "Thread-A").start();
        new Thread(phone::sendSMS, "Thread-B").start();

        CommonUtils.sleepSeconds(2);
    }

    /**
     * Thread-A ==> synchronized sendEmailWithSleep(seconds)
     * Thread-B ==> hello()
     *
     * Thread-A::Start sendEmailWithSleep(seconds)
     * Thread-B::Hello World!
     * Thread-A::Phone send email with sleep 1
     */
    @Test
    public void testLock03() {
        Phone phone = new Phone();

        new Thread(() -> phone.sendEmailWithSleep(1), "Thread-A").start();
        CommonUtils.sleepSeconds(1);
        new Thread(phone::helle, "Thread-B").start();

        CommonUtils.sleepSeconds(2);
    }

    /**
     * Thread-A ==> phone1 的 synchronized sendEmailWithSleep(seconds)
     * Thread-B ==> phone2 的 synchronized sendEmailWithSleep(seconds)
     *  phone1 and phone2 分别属于两个不同资源
     *
     * Thread-A::Start sendEmailWithSleep(seconds)
     * Thread-B::Start sendEmailWithSleep(seconds)
     * Thread-A::Phone send email with sleep 1
     * Thread-B::Phone send email with sleep 1
     */
    @Test
    public void testLock04() {
        Phone phone1 = new Phone();
        Phone phone2 = new Phone();

        new Thread(() -> phone1.sendEmailWithSleep(1), "Thread-A").start();
        new Thread(() -> phone2.sendEmailWithSleep(1), "Thread-B").start();

        CommonUtils.sleepSeconds(1);
    }

    private static void testLockExample04() {
        Phone phone1 = new Phone();
        Phone phone2 = new Phone();

        new Thread(phone1::sendEmail, "Thread-A").start();
        CommonUtils.sleepSeconds(1);
        new Thread(phone2::sendSMS, "Thread-B").start();

        CommonUtils.sleepSeconds(2);
    }

    @Test
    public void testLock05() {
        Phone phone = new Phone();
        new Thread(() -> phone.sendEmailStatic(), "Thread-A").start();

        new Thread(() -> phone.sendSMSStatic(), "Thread-B").start();

        CommonUtils.sleepSeconds(2);
    }

    @Test
    public void testLock06() {
        Phone phone1 = new Phone();
        Phone phone2 = new Phone();

        new Thread(() -> phone1.sendEmailStatic(), "Thread-A").start();

        new Thread(() -> phone2.sendSMSStatic(), "Thread-B").start();

        CommonUtils.sleepSeconds(2);
    }

    @Test
    public void testLock07() {
        Phone phone = new Phone();

        new Thread(() -> phone.sendEmailStatic(), "Thread-A").start();

        CommonUtils.sleepSeconds(1);

        new Thread(() -> phone.sendSMS(), "Thread-B").start();
    }

    @Test
    public void testLock08() {
        Phone phone1 = new Phone();
        Phone phone2 = new Phone();

        new Thread(() -> phone1.sendEmailStatic(), "Thread-A").start();
        new Thread(() -> phone2.sendSMS(), "Thread-B").start();
        CommonUtils.sleepSeconds(1);
    }
}


// 资源类
class Phone {
    public void helle() {
        System.out.println(Thread.currentThread().getName() + "::" + "Hello World!");
    }

    public synchronized void sendEmail() {
        System.out.println(Thread.currentThread().getName() + "::" + "Phone send email");
    }

    public synchronized void sendSMS() {
        System.out.println(Thread.currentThread().getName() + "::" + "Phone send sms");
    }

    public static synchronized void sendEmailStatic() {
        System.out.println(Thread.currentThread().getName() + "::" + "Phone sendEmailStatic");
    }

    public synchronized void sendEmailWithSleep(int seconds) {
        System.out.println(Thread.currentThread().getName() + "::" + "Start sendEmailWithSleep(seconds)");
        CommonUtils.sleepSeconds(seconds);
        System.out.println(Thread.currentThread().getName() + "::" + "Phone send email with sleep " + seconds);
    }

    public static synchronized void sendSMSStatic() {
        System.out.println(Thread.currentThread().getName() + "::" + "Phone sendSMSStatic");
    }
}