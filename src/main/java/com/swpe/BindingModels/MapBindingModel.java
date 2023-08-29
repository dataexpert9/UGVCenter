package com.swpe.BindingModels;

public class MapBindingModel {
    public int mapid;
    public String name;
    public String details;
    public String filepath;

//    public Float resolution;
//    public String origin;
//    public String origingps;
//    public int width;
//    public int length;

    public MapBindingModel(int mapid,String name,String details,String filepath, Float resolution,String origin,String origingps,int width,int length){
        this.mapid=mapid;
        this.name=name;
        this.details=details;
        this.filepath=filepath;

//        this.resolution=resolution;
//        this.origin=origin;
//        this.origingps=origingps;
//        this.width=width;
//        this.length=length;
    }
}
