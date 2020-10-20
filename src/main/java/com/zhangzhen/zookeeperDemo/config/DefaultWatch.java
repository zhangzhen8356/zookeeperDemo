package com.zhangzhen.zookeeperDemo.config;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.CountDownLatch;

/**
 * <p>功能描述：监控</p>
 * <p>类名称：com.zhangzhen.zookeeperDemo.config.DefaultWatch</p>
 * <p>创建时间：2020/10/18 20:11</p>
 * @author zzhen
 */
public class DefaultWatch implements Watcher {

    CountDownLatch countDownLatch;


    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        Event.KeeperState state = watchedEvent.getState();

        switch (state) {
            case Disconnected:
                System.out.println("Disconnected......new...");
                break;
            case SyncConnected:
                System.out.println("Connected......ok...");
                countDownLatch.countDown();
                break;
        }
    }
}
