package com.guodong.model;

/**
 * Created by yechy on 2015/12/10.
 */
public class UpdateInfo {
    private String versionName;
    private int versionCode;
    private String url;
    private String description;
    private String url_server;

    public String getVersionName() {
        return versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl_server() {
        return url_server;
    }

    public void setUrl_server(String url_server) {
        this.url_server = url_server;
    }
}
