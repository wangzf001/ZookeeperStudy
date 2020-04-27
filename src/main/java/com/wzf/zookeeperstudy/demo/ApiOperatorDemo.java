package com.wzf.zookeeperstudy.demo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
            Stat st = zooKeeper.exists("/wang5", new ApiOperatorDemo());
            if(st == null){
                String p = zooKeeper.create("/wang5", "wang".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//                String r = new String(zooKeeper.getData(p, new ApiOperatorDemo(), stat));
//                System.out.println("创建节点:" + r);
                TimeUnit.MILLISECONDS.sleep(2000);
            }

            //修改节点
            stat = zooKeeper.setData("/wang5", "wang1".getBytes(), 0);
//            System.out.println("STAT:" + stat.getVersion());
            TimeUnit.MILLISECONDS.sleep(2000);

            //修改节点
            zooKeeper.setData("/wang5","wang2".getBytes(),-1);

            //创建子节点,会触发create事件，临时节点不可以有子节点
            Stat s =zooKeeper.exists("/wang5/wang",new ApiOperatorDemo());
            if(s == null){
                zooKeeper.create("/wang5/wang","c".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);
            }

            List<String> childrens = zooKeeper.getChildren("/wang5",true);
            System.out.println("子节点："+childrens.toString());

            TimeUnit.MILLISECONDS.sleep(2000);
            //删除节点 ,不能直接删除父字节
            zooKeeper.delete("/wang5/wang", -1);
            zooKeeper.delete("/wang5", -1);
            TimeUnit.MILLISECONDS.sleep(2000);

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
                try {
                    System.out.println("新增路径：" + watchedEvent.getPath() + " 其值为" +
                            new String(zooKeeper.getData(watchedEvent.getPath(), true, stat)));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (watchedEvent.getType() == Event.EventType.NodeDataChanged) {
                try {
                    System.out.println(watchedEvent.getPath() + "修改数据,值为" +
                            new String(zooKeeper.getData(watchedEvent.getPath(), true, stat)));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
                System.out.println(watchedEvent.getType() + " 删除节点： " + watchedEvent.getPath() );

            } else if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                try {
                    System.out.println(watchedEvent.getType() + " 修改子节点： " + watchedEvent.getPath() +
                            zooKeeper.getData(watchedEvent.getPath(), true, stat));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //System.out.println(watchedEvent.getType() + " ELSE 当前状态：" + watchedEvent.getState());
        } else {
            System.out.println(watchedEvent.getType() + "当前状态：" + watchedEvent.getState());
        }


    }

}
