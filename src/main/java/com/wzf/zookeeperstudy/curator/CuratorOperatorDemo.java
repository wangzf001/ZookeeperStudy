package com.wzf.zookeeperstudy.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author 王振方
 * @date 2020/5/14
 */
public class CuratorOperatorDemo {

    public static void main(String[] args) {

        CuratorFramework curator = CuratorClientUtils.getInstance();


        try {
            //创建
//            curator.create().creatingParentsIfNeeded()
//                    .withMode(CreateMode.PERSISTENT).forPath("/demo2","2".getBytes());

            //删除
//            curator.delete()
//                    .deletingChildrenIfNeeded().forPath("/demo1");

            //修改
//            curator.setData().forPath("/demo2","3".getBytes());

            //查询
            /*Stat stat = new Stat();
            byte[] bytes = curator.getData().storingStatIn(stat).forPath("/demo2");
            System.out.println("值为："+new String(bytes));
            System.out.println("stat："+stat.toString());*/

            //异步操作
            /*CountDownLatch countDownLatch = new CountDownLatch(1);
            ExecutorService service = Executors.newFixedThreadPool(1);
            byte[] bytes = curator.getData().inBackground(new BackgroundCallback() {
                @Override
                public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) throws Exception {
                    System.out.println(Thread.currentThread().getName()+"->resultCode:"+curatorEvent.getResultCode()+"->"
                            +curatorEvent.getType());
                    countDownLatch.countDown();

                }
            },service).forPath("/demo2");
            countDownLatch.await();
            service.shutdown();*/


            //事务操作

            Collection<CuratorTransactionResult> transactionResults =
                    curator.inTransaction()
                        .create().forPath("/demo3","demo3".getBytes())
                        .and()
                        .setData().forPath("/demo2","demo2-1".getBytes())
                        .and().commit();
            for (CuratorTransactionResult transactionResult : transactionResults) {
                System.out.println(transactionResult.getForPath()+"->"+transactionResult.getType());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
