package com.swpe.BindingModels;

import java.util.Date;

public class UGVBindingModel {
    public int ugvid;
    public String ip;
    public String port;
    public String cameraip;
    public String name;
    public UGVBindingModel(int ugvid,String ip,String port,String cameraip, String name){
        this.ugvid=ugvid;
        this.ip=ip;
        this.cameraip=cameraip;
        this.name=name;
        this.port=port;
    }
}
