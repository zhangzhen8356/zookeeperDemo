package com.zhangzhen.zookeeperDemo.config;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * <p>功能描述：</p>
 * <p>类名称：com.zhangzhen.zookeeperDemo.config.WatchCallBack</p>
 * <p>创建时间：2020/10/18 20:35</p>
 *
 * @author zzhen
 */
public class WatchCallBack implements Watcher,AsyncCallback.DataCallback, AsyncCallback.StatCallback {

    private ZooKeeper zooKeeper;

    private String watchPath;

    private MyConf myconfInfo;

    private CountDownLatch countDownLatch;

    public void setMyconfInfo(MyConf myconfInfo) {
        this.myconfInfo = myconfInfo;
    }

    public MyConf getMyconfInfo() {
        return myconfInfo;
    }

    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    public void setWatchPath(String watchPath) {
        this.watchPath = watchPath;
    }

    /**
     * <p>Title: 等待，如果有可以获取到，没有需要阻塞</p>
     * <p>Create Time: 2020/10/18 20:53</p>
     * @author zhangzhen@bonc.com.cn
     */
    public void await(){
        try {
            zooKeeper.exists(watchPath, this, this, "initExists");
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>Title: 初始化锁</p>
     * <p>Create Time: 2020/10/18 20:53</p>
     * @author zhangzhen@bonc.com.cn
     */
    public void setInit(int init) {
        System.out.println("set init....");
        this.countDownLatch = new CountDownLatch(init);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>Title: 监视方法</p>
     * <p>Create Time: 2020/10/18 20:55</p>
     * @author zhangzhen@bonc.com.cn
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        Event.EventType type = watchedEvent.getType();
        switch (type) {
            case NodeCreated:
                System.out.println("...watch@Created");
                zooKeeper.getData(watchPath, this, this, "createNode");
                break;
            case NodeDeleted:
                System.out.println("...watch@Delete");
                myconfInfo.setMessage("");
                countDownLatch = new CountDownLatch(1);
                break;
            case NodeDataChanged:
                System.out.println("...watch@Change");
                zooKeeper.getData(watchPath, this, this, "NodeChanged");
                break;
            case NodeChildrenChanged:
                break;
        }
    }

    /**
     * <p>Title: getData的回调方法</p>
     * <p>Create Time: 2020/10/18 20:56</p>
     * @author zhangzhen@bonc.com.cn
     */
    @Override
    public void processResult(int i, String s, Object o, byte[] data, Stat stat) {
        if(Objects.nonNull(data)){
            myconfInfo.setMessage(new String(data));
            countDownLatch.countDown();
        }
    }

    /**
     * <p>Title: exists的回调方法</p>
     * <p>Create Time: 2020/10/18 20:56</p>
     * @author zhangzhen@bonc.com.cn
     */
    @Override
    public void processResult(int i, String s, Object o, Stat stat) {
        if (Objects.nonNull(stat)) {
            zooKeeper.getData(watchPath, this, this, "ex");
        }
    }
}
