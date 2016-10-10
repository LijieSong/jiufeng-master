package com.uniquedu.cemetery.utils;

public class UpdataInfo {
    private String version;
    private String url;  
    private String description;  
    public String getVersion() {  
        return version;
    }  
    public void setVersion(String version) {  
        this.version = version;
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

    private String UpdateTheContent;
    private String versionCode;
    public String getUpdateTheContent() {
        return UpdateTheContent;
    }

    public void setUpdateTheContent(String updateTheContent) {
        UpdateTheContent = updateTheContent;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

}