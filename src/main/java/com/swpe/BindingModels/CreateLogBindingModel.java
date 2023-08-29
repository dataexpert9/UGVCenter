package com.swpe.BindingModels;

public class CreateLogBindingModel {
    public int actiontype;
    public int subactiontype;
    public String description;



    public CreateLogBindingModel(int actiontype,int subactiontype,String description){
        this.actiontype=actiontype;
        this.subactiontype=subactiontype;
        this.description=description;
    }




}
