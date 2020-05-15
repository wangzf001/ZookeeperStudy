package com.wzf.zookeeperstudy.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.zookeeper.CreateMode;

/**
 * @author 王振方
 * @date 2020/5/14
 */
public class CuratorEventDemo {

    public static void main(String[] args) throws Exception {

        /**
         * 三种watcher 做节点的监听
         * pathcache 监听子节点的增删改
         * nodecache 监听节点的增删改
         * treecache   pathcache+nodecache
         */
        CuratorFramework curatorFramework = CuratorClientUtils.getInstance();

        NodeCache nodeCache = new NodeCache(curatorFramework,"/c1",false);
        nodeCache.start();

        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                System.out.println("节点变化后的结果："+nodeCache.getCurrentData().getData());
            }
        });

        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/event");



    }

}
