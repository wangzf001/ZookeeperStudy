package com.wzf.zookeeperstudy.demo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: HERO
 * @Date: 2020/4/25 15:43
 * @Version 1.0
 */
public class ApiOperatorDemo implements Watcher {
    static CountDownLatch countDownLatch = new CountDownLatch(1);
    static ZooKeeper zooKeeper;
    static Stat stat;

    public static void main(String[] args) {
        final String ZK_NET = "192.168.0.16:2181,192.168.0.17:2181,192.168.0.18:2181";

        try {
            zooKeeper = new ZooKeeper(ZK_NET, 5000, new ApiOperatorDemo());
            countDownLatch.await();

            //创建节点
            String r = zooKeeper.create("/wang2", "wang".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("创建结果：" + r);

            stat = zooKeeper.setData("/wang2","wang2".getBytes(),0);
            System.out.println("STAT:" + stat.toString());

            //删除节点
            zooKeeper.delete("/wang1", 0);

            //修改节点


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void process(WatchedEvent watchedEvent) {

        if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
            if (watchedEvent.getType() == Event.EventType.None) {
                countDownLatch.countDown();
                System.out.println(watchedEvent.getType() + " 连接成功：" + watchedEvent.getState());
            } else if (watchedEvent.getType() == Event.EventType.NodeCreated) {
                System.out.println("新增路径：" + watchedEvent.getPath());
            } else if (watchedEvent.getType() == Event.EventType.NodeDataChanged) {
                try {
                    System.out.println(watchedEvent.getPath() + "修改数据,值为" +
                            zooKeeper.getData(watchedEvent.getPath(), true, stat));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
                System.out.println(watchedEvent.getType() + " 删除节点： " + watchedEvent.getPath() );
            }


        }


    }

}
