package com.swpe.BindingModels;

public class SendOrderBindingModel {
    public int ugvid;
    public int OrderType;

    public SendOrderBindingModel(int ugvid,int OrderType){
        this.ugvid=ugvid;
        this.OrderType=OrderType;
    }


}
