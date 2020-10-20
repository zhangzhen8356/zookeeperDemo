package com.zhangzhen.zookeeperDemo.locks;

import com.zhangzhen.zookeeperDemo.config.DefaultWatch;
import com.zhangzhen.zookeeperDemo.config.ZKProperties;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * <p>功能描述：</p>
 * <p>类名称：com.zhangzhen.zookeeperDemo.config.ZKUtils</p>
 * <p>创建时间：2020/10/18 20:03</p>
 * @author zzhen
 */
public class ZKUtils {

    private static ZooKeeper zookeeper;

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    /**
     * <p>Title: 获取zk对象</p>
     * <p>Create Time: 2020/10/18 20:14</p>
     * @author zhangzhen@bonc.com.cn
     */
    public static ZooKeeper getZK(ZKProperties properties, DefaultWatch watcher){

        try {
            if(Objects.isNull(zookeeper)){
                watcher.setCountDownLatch(countDownLatch);
                zookeeper = new ZooKeeper(properties.getAddress(), properties.getSessionTime(), watcher);
                countDownLatch.await();
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return zookeeper;
    }


    /**
     * <p>Title: zk连接关闭</p>
     * <p>Create Time: 2020/10/18 20:16</p>
     * @author zhangzhen@bonc.com.cn
     */
    public static void close(){
        if (Objects.nonNull(zookeeper)) {
            try {
                zookeeper.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
