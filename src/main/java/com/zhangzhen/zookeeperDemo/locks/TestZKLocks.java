package com.zhangzhen.zookeeperDemo.locks;

import com.zhangzhen.zookeeperDemo.config.DefaultWatch;
import com.zhangzhen.zookeeperDemo.config.ZKProperties;
import com.zhangzhen.zookeeperDemo.config.ZKUtils;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>功能描述：</p>
 * <p>类名称：com.zhangzhen.zookeeperDemo.locks.TestZKLocks</p>
 * <p>创建时间：2020/10/19 10:45</p>
 * @author zzhen
 */
public class TestZKLocks {

    private ZooKeeper zooKeeper;

    @Before
    public void init(){
        String address = "node01:2181,node02:2181,node03:2181/testLocks";
        Integer sessionTime = 1000;
        com.zhangzhen.zookeeperDemo.config.ZKProperties zkProperties = new ZKProperties();
        zkProperties.setAddress(address);
        zkProperties.setSessionTime(sessionTime);
        com.zhangzhen.zookeeperDemo.config.DefaultWatch defaultWatch = new DefaultWatch();
        zooKeeper = com.zhangzhen.zookeeperDemo.config.ZKUtils.getZK(zkProperties, defaultWatch);
    }

    @After
    public void close(){
        ZKUtils.close();
    }

    @Test
    public void test(){
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                WatchCallBack watchCallBack = new WatchCallBack();
                watchCallBack.setZooKeeper(zooKeeper);
                watchCallBack.setThreadName(Thread.currentThread().getName());
                watchCallBack.tryLock();
                System.out.println(Thread.currentThread().getName() + "正在工作");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                watchCallBack.unLock();
            }, "threadName-" + (i+1)).start();

        }

        while (true){

        }
    }

}
