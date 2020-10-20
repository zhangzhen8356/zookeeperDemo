package com.zhangzhen.zookeeperDemo.config;

import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

/**
 * <p>功能描述：测试配置中心</p>
 * <p>类名称：com.zhangzhen.zookeeperDemo.config.TestZKConf</p>
 * <p>创建时间：2020/10/18 20:19</p>
 * @author zzhen
 */
public class TestZKConf {

    private ZooKeeper zooKeeper;

    @Before
    public void init(){
        String address = "node01:2181,node02:2181,node03:2181/testConf";
        Integer sessionTime = 1000;
        ZKProperties zkProperties = new ZKProperties();
        zkProperties.setAddress(address);
        zkProperties.setSessionTime(sessionTime);
        DefaultWatch defaultWatch = new DefaultWatch();
        zooKeeper = ZKUtils.getZK(zkProperties, defaultWatch);
    }

    @After
    public void close(){
        ZKUtils.close();
    }

    @Test
    public void start() throws InterruptedException {

        MyConf myConf = new MyConf();
        WatchCallBack watchCallBack = new WatchCallBack();
        watchCallBack.setInit(1);
        watchCallBack.setMyconfInfo(myConf);
        watchCallBack.setWatchPath("/myConf33");
        watchCallBack.setZooKeeper(zooKeeper);

        watchCallBack.await();

        while (true) {
            if (Objects.isNull(myConf.getMessage()) || Objects.equals(myConf.getMessage(), "")) {
                System.out.println("diu le====");
                watchCallBack.await();
            } else {
                System.out.println(myConf.getMessage());
            }
            Thread.sleep(200);
        }

    }
}
