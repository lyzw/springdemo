package com.zhouw.demo.sync;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * 继承线程类方式实现多线程
 * Created by zhouwei on 2016/9/12.
 */
public class MyThread extends Thread{
    private static Logger logger = LogManager.getLogger(MyThread.class);
    MyThread(int i){
        this.setName(String.valueOf(i));
    }
    public void run(){
        int i = 0;
        while (i<=100){
            System.out.println("["+ Thread.currentThread().getName() + "]线程，第"+ i+"次执行");
            i ++ ;
        }
    }
    public static void main(String[] args){
        new MyThread(1).start();
        new MyThread(2).start();
        new MyThread(3).start();
        new MyThread(4).start();
        new MyThread(5).start();
    }

}
