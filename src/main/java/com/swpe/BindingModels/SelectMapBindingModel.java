package com.swpe.BindingModels;

public class SelectMapBindingModel {
//    public int ugvid;
    public int[] ugvids;
    public int mapid;

    public SelectMapBindingModel(int[] ugvids,int mapid){
//        this.ugvid=ugvid;
        this.ugvids=ugvids;
        this.mapid=mapid;
    }


}
