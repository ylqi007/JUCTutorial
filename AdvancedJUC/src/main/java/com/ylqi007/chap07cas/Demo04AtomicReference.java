package com.ylqi007.chap07cas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author: ylqi007
 * @Date: 8/19/25  9:38 PM
 * @Description:
 */
public class Demo04AtomicReference {
    public static void main(String[] args) {
        AtomicReference<User> atomicReference = new AtomicReference<>();

        User zhangSan = new User("ZhangSan", 18);
        User liSi = new User("LiSi", 19);

        atomicReference.set(zhangSan);

        // true	User(name=LiSi, age=19)
        System.out.println(atomicReference.compareAndSet(zhangSan, liSi) + "\t" + atomicReference.get());

        // false	User(name=LiSi, age=19)
        System.out.println(atomicReference.compareAndSet(zhangSan, liSi) + "\t" + atomicReference.get());



    }
}


@AllArgsConstructor
@Data
@ToString
class User {
    private String name;
    private int age;
}
