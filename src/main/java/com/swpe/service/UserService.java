package com.swpe.service;

import com.alibaba.fastjson.JSONObject;

import com.swpe.BindingModels.ChangePasswordBindingModel;
import com.swpe.BindingModels.LoginBindingModel;
import com.swpe.entity.User;

import java.util.List;

public interface UserService {

    JSONObject test(String json);
    User Login(LoginBindingModel model);
    boolean ChangePassword(int id, ChangePasswordBindingModel model);
    List<User> GetAllUsers();
    User GetUserById(int id);
    User UsernameExists(String Username);
    int UsernameExist(String Username);
    int CheckUsernameForEditUser(int id,String Username);



}
