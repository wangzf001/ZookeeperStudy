package com.wzf.zookeeperstudy.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author 王振方
 * @date 2020/5/14
 */
public class CuratorClientUtils {

    static final String ZK_NET = "172.16.2.106:2181,172.16.2.182:2181,172.16.2.98:2181";

    public static CuratorFramework getInstance(){
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(ZK_NET)
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(5000,5))
                .namespace("curator1")
                .build();
        curatorFramework.start();
        return curatorFramework;
    }

}
