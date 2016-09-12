package com.zhouw.demo.sync;

import java.util.concurrent.Callable;

/**
 * Created by zhouwei on 2016/9/12.
 */
public class CallableThread implements Callable {

    public Object call() throws Exception {
        int i = 0;
        while (i<=100){
            System.out.println("["+ Thread.currentThread().getName() + "]线程，第"+ i+"次执行");
            i ++ ;
        }
        return i;
    }

    public static void main(String[] args) throws Exception {
        Object future1 = new CallableThread().call();
        Object future2 = new CallableThread().call();
        Object future3 = new CallableThread().call();
        Object future4 = new CallableThread().call();
        Object future5 = new CallableThread().call();
        System.out.println("future1 == " + future1);
        System.out.println("future1 == " + future2);
        System.out.println("future1 == " + future3);
        System.out.println("future1 == " + future4);
        System.out.println("future1 == " + future5);

    }
}
