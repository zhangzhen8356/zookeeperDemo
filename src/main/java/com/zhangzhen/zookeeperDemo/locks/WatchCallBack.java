package com.zhangzhen.zookeeperDemo.locks;

import org.apache.zookeeper.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * <p>功能描述：</p>
 * <p>类名称：com.zhangzhen.zookeeperDemo.locks.WatchCallBack</p>
 * <p>创建时间：2020/10/19 10:35</p>
 *
 * @author zzhen
 */
public class WatchCallBack implements Watcher,AsyncCallback.StringCallback,AsyncCallback.ChildrenCallback {

    private ZooKeeper zooKeeper;

    private String lockName;

    private String pathPrefix = "/lock";

    private String threadName;

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    /**
     * <p>Title: 尝试获取锁</p>
     * <p>Create Time: 2020/10/19 10:36</p>
     * @author zhangzhen@bonc.com.cn
     */
    public void tryLock(){
        try {
            zooKeeper.create(pathPrefix, threadName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL, this, threadName);
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>Title: 释放锁</p>
     * <p>Create Time: 2020/10/19 11:02</p>
     * @author zhangzhen@bonc.com.cn
     */
    public void unLock(){
        try {
            zooKeeper.delete("/" + lockName, -1);
            System.out.println(threadName + " ===== end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        Event.EventType type = watchedEvent.getType();
        switch (type) {
            case NodeDeleted:
                zooKeeper.getChildren("/", false, this, "");
                break;
            case NodeChildrenChanged:
                break;
        }
    }


    /**
     * <p>Title: create的回调方法，节点创建成功</p>
     * <p>Create Time: 2020/10/19 10:39</p>
     * @author zhangzhen@bonc.com.cn
     */
    @Override
    public void processResult(int i, String pathPrefix, Object ctx, String name) {
        System.out.println("create:" + threadName + "create path:" + name);
        lockName = name.substring(1);
        zooKeeper.getChildren("/", false, this, name);
    }

    /**
     * <p>Title: getChild的回调方法</p>
     * <p>Create Time: 2020/10/19 11:14</p>
     * @author zhangzhen@bonc.com.cn
     */
    @Override
    public void processResult(int i, String s, Object o, List<String> list) {
        if (list.isEmpty()) {
            System.out.println(o.toString() + "list is null");
        } else {
            Collections.sort(list);
            int index = list.indexOf(lockName);
            if(index < 1){
                System.out.println(threadName + "   is first...");
                countDownLatch.countDown();
            } else {
                try {
                    //System.out.println("getChild" + threadName + "watch " + list.get(index-1));
                    zooKeeper.exists("/" + list.get(index-1), this);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
