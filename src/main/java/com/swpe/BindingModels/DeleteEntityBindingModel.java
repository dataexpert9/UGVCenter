package com.swpe.BindingModels;

import org.hibernate.sql.Delete;

public class DeleteEntityBindingModel {

    public int id;
    public int type;


    public DeleteEntityBindingModel(int id,int type){
        this.id=id;
        this.type=type;
    }




}
