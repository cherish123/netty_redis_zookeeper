package com.crazymakercircle.netty.imooc.design;

import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.codec.mqtt.MqttEncoder;

/**
 * @see ReadTimeoutException
 * @see MqttEncoder
 */
public class Singleton {

    //第一种方式创建单例
    //private static Singleton singleton;

    private Singleton() {
    }

    /*public static Singleton getInstance(){
        if (singleton == null) {
            synchronized (Singleton.class) {
                *//**
                 * 二次判断是以防A.B线程都同时执行了加锁
                 *//*
                if (singleton == null) {
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }*/

    //第二种方式创建单例
    public static final Singleton INSTANCE = new Singleton();
}
