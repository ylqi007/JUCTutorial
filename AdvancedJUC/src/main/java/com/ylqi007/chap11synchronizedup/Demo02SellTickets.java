package com.ylqi007.chap11synchronizedup;

/**
 * @Author: ylqi007
 * @Date: 8/23/25  11:48 PM
 * @Description:
 */
public class Demo02SellTickets {
    public static void main(String[] args) {
        Ticket ticket = new Ticket();

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                for (int j = 0; j < 55; j++) {
                    ticket.sellTicket();
                }
            }, "t" + i).start();
        }
    }
}

class Ticket {
    private int number = 50;

    Object object = new Object();

    public void sellTicket() {
        synchronized (object) {
            if(number > 0) {
                System.out.println(Thread.currentThread().getName() + "\t" + "卖出\t"  + (number--) + "\t还剩下：" + number);
            }
        }
    }
}
