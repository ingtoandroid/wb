package com.example.a.app10.bean;

/**
 * Created by 12917 on 2017/6/13.
 */

public class URLString {

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPath1() {
        return path1;
}

    public void setPath1(String path1) {
        this.path1 = path1;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getPath2() {
        return path2;
    }

    public void setPath2(String path2) {
        this.path2 = path2;
    }

    private String protocol = "http:";
    private String hostname = "192.168.1.143";
    private String port = "8080";
    private String path1 = "yjtyms/yjty_App/";
    private String path2 = "";
    private String parameters = "";
    private String delimiter = "\\";
}
