package com.swpe.BindingModels;

import javax.validation.constraints.Null;

public class AddUserBindingModel {

    public int userid;
    public String username;
    public int role;
    public String name;

    public AddUserBindingModel(int userid,String username,int role,String name){
        this.userid=userid;
        this.username=username;
        this.role=role;
        this.name=name;
    }

}
