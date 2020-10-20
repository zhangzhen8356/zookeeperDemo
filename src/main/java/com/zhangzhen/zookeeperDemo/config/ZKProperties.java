package com.zhangzhen.zookeeperDemo.config;

/**
 * <p>功能描述：zk配置属性</p>
 * <p>类名称：com.zhangzhen.zookeeperDemo.config.ZKProperties</p>
 * <p>创建时间：2020/10/18 20:06</p>
 * @author zzhen
 */
public class ZKProperties {

    /**
     * zk地址
     */
    private String address;

    /**
     * session
     */
    private Integer sessionTime;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(Integer sessionTime) {
        this.sessionTime = sessionTime;
    }
}
