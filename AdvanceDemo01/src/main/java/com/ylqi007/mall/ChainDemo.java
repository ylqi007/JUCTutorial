package com.ylqi007.mall;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class ChainDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        testGet();
        testJoin();
    }

    private static void testGet() throws ExecutionException, InterruptedException {
        Student student = new Student();
        student.setId(11);
        student.setName("李四");
        student.setMajor("Unknown");

        System.out.println(student);

        // 链式调用，是一种新的语法
        student.setId(12).setName("张三").setMajor("Math");

        CompletableFuture<Student> studentCompletableFuture = CompletableFuture.supplyAsync(() -> {
            return student;
        });

        System.out.println(studentCompletableFuture.get());   // get() 会抛出异常
    }

    private static void testJoin() {
        Student student = new Student();
        System.out.println(student);

        // 链式调用，是一种新的语法
        student.setId(12).setName("张三").setMajor("Math");
        System.out.println(student);

        CompletableFuture<Student> studentCompletableFuture = CompletableFuture.supplyAsync(() -> student);

        System.out.println(studentCompletableFuture.join());    // join() 不会抛出异常
    }
}


@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)    // 链式调用
class Student {
    private Integer id;
    private String name;
    private String major;
}
