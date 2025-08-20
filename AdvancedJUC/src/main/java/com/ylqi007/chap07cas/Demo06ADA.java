package com.ylqi007.chap07cas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @Author: ylqi007
 * @Date: 8/19/25  10:10 PM
 * @Description:
 */
public class Demo06ADA {
    public static void main(String[] args) {
        Book javaBook = new Book(1, "Java Basics");

        AtomicStampedReference stampedReference = new AtomicStampedReference(javaBook, 1);

        System.out.println(stampedReference.getReference() + "\t" + stampedReference.getStamp());

        Book mySQLBook = new Book(2, "MySQL");

        boolean b;
        b = stampedReference.compareAndSet(javaBook, mySQLBook, stampedReference.getStamp(), stampedReference.getStamp() + 1);
        System.out.println(b + "\t" + stampedReference.getReference() + "\t" + stampedReference.getStamp());

        b = stampedReference.compareAndSet(mySQLBook, javaBook, stampedReference.getStamp(), stampedReference.getStamp() + 1);
        System.out.println(b + "\t" + stampedReference.getReference() + "\t" + stampedReference.getStamp());

    }
}


@Data
@AllArgsConstructor
@ToString
class Book {
    private int id;
    private String name;
}