package com.zhouw.demo.sync;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by zhouwei on 2016/9/12.
 */
public class CallableThread implements Callable {

    public Integer call() throws Exception {
        int i = 0;
        while (i<=5){
            System.out.println("["+ Thread.currentThread().getName() + "]线程，第"+ i+"次执行");
            i ++ ;
            Thread.sleep(1000);
        }
        return i;
    }

    public static void main(String[] args) throws Exception {
        CallableThread task1 = new CallableThread();
        CallableThread task2 = new CallableThread();
        CallableThread task3 = new CallableThread();
        CallableThread task4 = new CallableThread();
        CallableThread task5 = new CallableThread();
        FutureTask<Integer> futureTask1 = new FutureTask<Integer>(task1);
        FutureTask<Integer> futureTask2 = new FutureTask<Integer>(task2);
        FutureTask<Integer> futureTask3 = new FutureTask<Integer>(task3);
        FutureTask<Integer> futureTask4 = new FutureTask<Integer>(task4);
        FutureTask<Integer> futureTask5 = new FutureTask<Integer>(task5);
        new Thread(futureTask1,"1").start();
        new Thread(futureTask2,"2").start();
        new Thread(futureTask3,"3").start();
        new Thread(futureTask4,"4").start();
        new Thread(futureTask5,"5").start();
        try {
            System.out.println(futureTask1.get(1, TimeUnit.SECONDS).intValue());
        }catch (TimeoutException e){
            e.printStackTrace();
        }
        try {
            System.out.println(futureTask2.get(3, TimeUnit.SECONDS).intValue());
        }catch (TimeoutException e){
            e.printStackTrace();
        }
        try {
            System.out.println(futureTask3.get(5, TimeUnit.SECONDS).intValue());
        }catch (TimeoutException e){
            e.printStackTrace();
        }
        try {
            System.out.println(futureTask4.get(7, TimeUnit.SECONDS).intValue());
        }catch (TimeoutException e){
            e.printStackTrace();
        }
        try {
            System.out.println(futureTask5.get(9, TimeUnit.SECONDS).intValue());
        }catch (TimeoutException e){
            e.printStackTrace();
        }
    }
}
