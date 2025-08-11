package com.ylqi007.chap03locks.sync;

import com.ylqi007.utils.CommonUtils;

/**
 * 总结说明：
 * test01() and test02()：使用的都是同一个 `phone` 实例作为锁
 *  *  一个对象有多个`synchronized`方法时，某一时刻，只要有一个线程去调用其中的一个`synchronized`方法，其他的线程都只能等待。
 *  *  换句话说，某一时刻，只能有唯一的一个线程去访问**这些** `synchronized` 方法
 *  *  锁的是当前对象`this`，被锁定后，其他的线程都不能进入到当前对象的其他`synchronized` 方法
 *
 * test03() & test04()
 *  *  加上普通方法，发现和同步锁无关。
 *  *  换成两个对象后，不是同一把锁，互不影响
 *
 * test05() & test06()
 *  *  static synchronized 是类锁, phone1 and phone2 are created from the same template, i.e. the same class
 *
 * 5-6 都换成静态同步方法后，情况又变化: 三种 synchronized 锁的内容有一些差别:
 *   * 对于普通同步方法，锁的是当前实例对象，通常指this,具体的一部部手机,所有的普通同步方法用的都是同一把锁——实例对象本身，
 *   * 对于静态同步方法，锁的是当前类的Class对象，如Phone.class唯一的一个模板
 *   * 对于同步方法块，锁的是 synchronized 括号内的对象
 *
 * test07() & test08()
 *  当一个线程试图访问同步代码时它首先必须得到锁，退出或抛出异常时必须释放锁。
 *  * 所有的普通同步方法用的都是同一把锁——实例对象本身，就是new出来的具体实例对象本身,本类this
 *    也就是说如果一个实例对象的普通同步方法获取锁后，该实例对象的其他普通同步方法必须等待获取锁的方法释放锁后才能获取锁
 *  * 所有的静态同步方法用的也是同一把锁——类对象本身，就是我们说过的唯一模板Class
 *    * 具体实例对象this和唯一模板Class，这两把锁是两个不同的对象，所以静态同步方法与普通同步方法之间是不会有竞态条件的
 *    * 但是一旦一个静态同步方法获取锁后，其他的静态同步方法都必须等待该方法释放锁后才能获取锁。
 */
public class Synchronized8Demo {
    public static void main(String[] args) {
        // test01();
        // test02();

        // test03();
        // test04();

        // test05();
        // test06();

        // test07();
        test08();
    }

    /**
     * Case 1: 标准访问有 A,B 两个线程，请问先打印邮件还是短信？
     * Thread-A::Complete sendEmail()
     * Thread-B::Complete sendSMS()
     * 以上结果说明，Thread-A先完成，Thread-B才开始
     */
    private static void test01() {
        Phone phone = new Phone();

        new Thread(phone::sendEmail, "Thread-A").start();

        // 保证 Thread-A 先启动
        CommonUtils.sleepSeconds(1);

        new Thread(phone::sendSMS, "Thread-B").start();
    }

    /**
     * Case 2: sendEmail方法暂停3秒钟，请问先打印邮件还是短信?
     * Thread-A::Complete sendEmail()
     * Thread-B::Complete sendSMS()
     * 以上结果说明，Thread-A先完成，Thread-B才开始
     */
    private static void test02() {
        Phone phone = new Phone();

        new Thread(() -> phone.sendEmail(), "Thread-A").start();

        // 保证 Thread-A 先启动
        CommonUtils.sleepSeconds(1);

        new Thread(phone::sendSMS, "Thread-B").start();
    }

    /**
     * Case 3: 新增一个普通的hello方法，请问先打印邮件还是hello？
     * Thread-B::Hello World!
     * Thread-A::Complete sendEmail()
     * 以上结果说明：Thread-B 并不受 Thread-A 的影响
     */
    private static void test03() {
        Phone phone = new Phone();

        new Thread(phone::sendEmail, "Thread-A").start();

        // 保证 Thread-A 先启动
        CommonUtils.sleepSeconds(1);

        new Thread(phone::sayHello, "Thread-B").start();
    }

    /**
     * Case 4: 有两部手机，请问先打印邮件还是短信？
     * Thread-B::Complete sendSMS()
     * Thread-A::Complete sendEmail()
     * 以上结果说明：Thread-B 并不受 Thread-A 的影响。
     */
    private static void test04() {
        Phone phone1 = new Phone();
        Phone phone2 = new Phone();

        new Thread(phone1::sendEmail, "Thread-A").start();

        // 保证 Thread-A 先启动
        CommonUtils.sleepSeconds(1);

        new Thread(phone2::sendSMS, "Thread-B").start();
    }

    /**
     * Case 5: 两个静态同步方法，同1部手机，请问先打印邮件还是短信?
     * Thread-A::static::Complete sendEmailStatic()
     * Thread-B::static::Complete sendSMSStatic()
     * 以上结果说明：Thread-B 在 Thread-A 结束之后才开始
     */
    private static void test05() {
        Phone phone = new Phone();

        new Thread(() -> phone.sendEmailStatic(), "Thread-A").start();

        // 保证 Thread-A 先启动
        CommonUtils.sleepSeconds(1);

        new Thread(() -> phone.sendSMSStatic(), "Thread-B").start();
    }

    /**
     * Case 6: 两个静态同步方法， 2部手机，请问先打印邮件还是短信?
     * Thread-A::static::Complete sendEmailStatic()
     * Thread-B::static::Complete sendSMSStatic()
     * 以上结果说明：Thread-B 在 Thread-A 结束之后才开始
     */
    private static void test06() {
        Phone phone1 = new Phone();
        Phone phone2 = new Phone();

        new Thread(() -> phone1.sendEmailStatic(), "Thread-A").start();

        // 保证 Thread-A 先启动
        CommonUtils.sleepSeconds(1);

        new Thread(() -> phone2.sendSMSStatic(), "Thread-B").start();
    }

    /**
     * Case 7: 1个静态同步方法，1个普通同步方法, 同1部手机，请问先打印邮件还是短信?
     * Thread-B::Complete sendSMS()
     * Thread-A::static::Complete sendEmailStatic()
     * 以上结果说明：Thread-B 不受 Thread-A 的影响
     */
    private static void test07() {
        Phone phone = new Phone();

        new Thread(() -> phone.sendEmailStatic(), "Thread-A").start();

        // 保证 Thread-A 先启动
        CommonUtils.sleepSeconds(1);

        new Thread(() -> phone.sendSMS(), "Thread-B").start();
    }

    /**
     * Case 8: 1个静态同步方法，1个普通同步方法,2部手机，请问先打印邮件还是短信
     * Thread-B::Complete sendSMS()
     * Thread-A::static::Complete sendEmailStatic()
     * 以上结果说明：Thread-B 不受 Thread-A 的影响
     */
    private static void test08() {
        Phone phone1 = new Phone();
        Phone phone2 = new Phone();

        new Thread(() -> phone1.sendEmailStatic(), "Thread-A").start();

        // 保证 Thread-A 先启动
        CommonUtils.sleepSeconds(1);

        new Thread(() -> phone2.sendSMS(), "Thread-B").start();
    }
}


class Phone {

    public static synchronized void sendEmailStatic() {
        CommonUtils.sleepSeconds(3);
        System.out.println(Thread.currentThread().getName() + "::static::" + "Complete sendEmailStatic()");
    }

    public static synchronized void sendSMSStatic() {
        System.out.println(Thread.currentThread().getName() + "::static::" + "Complete sendSMSStatic()");
    }

    public synchronized void sendEmail() {
        CommonUtils.sleepSeconds(3);    // test01() 不等 3s；test02() 等 3s
        System.out.println(Thread.currentThread().getName() + "::" + "Complete sendEmail()");
    }

    public synchronized void sendSMS() {
        System.out.println(Thread.currentThread().getName() + "::" + "Complete sendSMS()");
    }

    public void sayHello() {
        System.out.println(Thread.currentThread().getName() + "::Hello World!");
    }
}
