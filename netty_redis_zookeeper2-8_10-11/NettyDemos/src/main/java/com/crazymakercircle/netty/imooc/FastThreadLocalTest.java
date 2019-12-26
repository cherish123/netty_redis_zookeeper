package com.crazymakercircle.netty.imooc;

import io.netty.util.concurrent.FastThreadLocal;

public class FastThreadLocalTest {

    private static FastThreadLocal<Object> threadLocal =
            new FastThreadLocal<Object>() {

                @Override
                protected Object initialValue() throws Exception {
                    return new Object();
                }
            };

    public static void main(String args[]) {
        new Thread(() -> {
            Object object = threadLocal.get();
            //....do with object
            System.out.println(object);

            while (true) {
                threadLocal.set(new Object());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            Object object = threadLocal.get();
            //....do with object
            System.out.println(object);

            while (true) {
                System.out.println(threadLocal.get() == object);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
