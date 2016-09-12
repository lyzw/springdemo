package com.zhouw.demo.sync;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 以实现Runnable接口方式实现多线程
 * Created by zhouwei on 2016/9/12.
 */
public class MyRunnableThread implements Runnable {
    private static Logger logger = LogManager.getLogger(MyThread.class);

    public void run(){
        int i = 0;
        while (i<=100){
            System.out.println("["+ Thread.currentThread().getName() + "]线程，第"+ i+"次执行");
            i ++ ;
        }
    }
    public static void main(String[] args){
        new Thread(new MyRunnableThread(),"1").start();
        new Thread(new MyRunnableThread(),"2").start();
        new Thread(new MyRunnableThread(),"3").start();
        new Thread(new MyRunnableThread(),"4").start();
        new Thread(new MyRunnableThread(),"5").start();
    }
}
