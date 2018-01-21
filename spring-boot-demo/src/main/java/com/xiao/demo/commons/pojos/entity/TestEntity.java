package com.xiao.demo.commons.pojos.entity;

import java.io.Serializable;

/**
 * Created by xiaoliang on 2018/1/4.
 */
public class TestEntity implements Serializable{

    public TestEntity(String id, String key_work, String key_url, String timestamp) {
        this.id = id;
        this.key_work = key_work;
        this.key_url = key_url;
        this.timestamp = timestamp;
    }

    public TestEntity() {
    }

    private String id;
    private String key_work;
    private String key_url;
    private String timestamp;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey_work() {
        return key_work;
    }

    public void setKey_work(String key_work) {
        this.key_work = key_work;
    }

    public String getKey_url() {
        return key_url;
    }

    public void setKey_url(String key_url) {
        this.key_url = key_url;
    }
}
