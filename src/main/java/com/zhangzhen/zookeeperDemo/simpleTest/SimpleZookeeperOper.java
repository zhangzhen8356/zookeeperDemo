package com.zhangzhen.zookeeperDemo.simpleTest;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * <p>功能描述：简单zk操作</p>
 * <p>类名称：com.zhangzhen.zookeeperDemo.simpleTest.SimpleZookeeperOper</p>
 * <p>创建时间：2020/10/18 17:28</p>
 * @author zzhen
 */
public class SimpleZookeeperOper {

    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {

        ZooKeeper zooKeeper = new ZooKeeper("node01:2181,node02:2181,node03:2181", 3000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                Event.EventType type = watchedEvent.getType();
                Event.KeeperState state = watchedEvent.getState();
                System.out.println("new zk watch: "+ watchedEvent.toString());
                switch (state) {
                    case Unknown:
                        break;
                    case Disconnected:
                        break;
                    case NoSyncConnected:
                        break;
                    case SyncConnected:
                        System.out.println("i am synConnect");
                        countDownLatch.countDown();
                        break;
                    case AuthFailed:
                        break;
                    case ConnectedReadOnly:
                        break;
                    case SaslAuthenticated:
                        break;
                    case Expired:
                        break;
                    case Closed:
                        break;
                }

                switch (type) {
                    case None:
                        break;
                    case NodeCreated:
                        System.out.println("i am create !");
                        break;
                    case NodeDeleted:
                        break;
                    case NodeDataChanged:
                        break;
                    case NodeChildrenChanged:
                        break;
                    case DataWatchRemoved:
                        break;
                    case ChildWatchRemoved:
                        break;
                }
            }

        });
        countDownLatch.await();
        final ZooKeeper.States state = zooKeeper.getState();
        switch (state) {
            case CONNECTING:
                System.out.println("...........ing");
                break;
            case ASSOCIATING:
                break;
            case CONNECTED:
                System.out.println("............ed");
                break;
            case CONNECTEDREADONLY:
                break;
            case CLOSED:
                break;
            case AUTH_FAILED:
                break;
            case NOT_CONNECTED:
                break;
        }

        //同步创建节点
        String s = zooKeeper.create("/test/aaabbb", "this is a good boy".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("create path" + s);

        //同步获取节点数据
        byte[] data = zooKeeper.getData(s, false, new Stat());

        System.out.println(new String(data));

        //异步方式，不带监控，获取节点数据
        zooKeeper.getData(s, false, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {
                System.out.println("===========this is huidiao  sync start=============");
                System.out.println(stat);
                System.out.println(i);
                System.out.println(s);
                System.out.println(new String(bytes));
                System.out.println("===========this is huidiao sync end=============");
            }
        }, "abc");

        System.out.println("i  am sync check!");

        //异步方式，带监控，获取节点数据
        zooKeeper.getData(s, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                Event.KeeperState state1 = watchedEvent.getState();
                Event.EventType type = watchedEvent.getType();
                switch (type) {
                    case None:
                        break;
                    case NodeCreated:
                        System.out.println("i am create !");
                        break;
                    case NodeDeleted:
                        System.out.println("i am delete!");
                        break;
                    case NodeDataChanged:
                        System.out.println("i am update");
                        break;
                    case NodeChildrenChanged:
                        break;
                    case DataWatchRemoved:
                        break;
                    case ChildWatchRemoved:
                        break;
                }

            }
        }, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int i, String s, Object o, byte[] bytes, Stat stat) {
                System.out.println("第二次回调了！");
            }
        }, new Stat());


        while (true){

        }

    }
}
