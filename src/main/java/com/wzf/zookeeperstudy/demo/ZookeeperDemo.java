package com.wzf.zookeeperstudy.demo;

import javafx.event.EventType;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: HERO
 * @Date: 2020/4/25 14:02
 * @Version 1.0
 */
public class ZookeeperDemo {

    public static void main(String[] args) throws IOException, InterruptedException {

        String ZK_NET = "192.168.0.16:2181,192.168.0.17:2181,192.168.0.18:2181";


        CountDownLatch countDownLatch = new CountDownLatch(1);
        ZooKeeper zooKeeper = new ZooKeeper(ZK_NET, 5000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if(watchedEvent.getState() == Event.KeeperState.SyncConnected){
                    countDownLatch.countDown();
                    System.out.println(watchedEvent.getState());
                }
            }
        });
        countDownLatch.await();
        System.out.println("连接状态："+zooKeeper.getState());

    }

}
