package com.wzf.zookeeperstudy.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.retry.RetryUntilElapsed;

/**
 * @author 王振方
 * @date 2020/5/14
 */
public class CuratorCreateSessionDemo {
    static final String ZK_NET = "172.16.2.106:2181,172.16.2.182:2181,172.16.2.98:2181";

    public static void main(String[] args) {

        /**
         * ExponentialBackoffRetry 衰减重试
         * RetryOneTime() 重试一次
         * RetryNTimes() 重试指定次数
         * RetryUntilElapsed() 重试指定时间
         *
         */
        CuratorFramework curator = CuratorFrameworkFactory.newClient(
                ZK_NET, 5000, 5000,
                new ExponentialBackoffRetry(2000,5));

        curator.start();

        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(ZK_NET)
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(new RetryUntilElapsed(10000,500))
                .namespace("curator_framwork")
                .build();

        curatorFramework.start();
    }


}
