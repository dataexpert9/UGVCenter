package com.swpe.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.swpe.BindingModels.*;
import com.swpe.BindingModels.LoginBindingModel;
import com.swpe.dao.UserRepository;
import com.swpe.entity.User;
import com.swpe.service.UserService;
import com.swpe.util.JwtUtil;
import com.swpe.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    public static final int MAX_FAILED_ATTEMPTS = 3;

    private static final long LOCK_TIME_DURATION = 24 * 60 * 60 * 1000; // 24 hours

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    UserRepository userRepository;


    @Override
    public JSONObject test(String json) {
        JSONObject ret = new JSONObject();

        ret.put("result",json);


        return ret;
    }

    public User Login(LoginBindingModel model){

        try {
            User user=userRepository.Login(model.username,model.password);

            if(user != null){
                return user;
            }else{
                return null;
            }
        }catch (Exception ex){
            return null;
        }
    }

    public User UsernameExists(String Username){
        try {
            User user=userRepository.UsernameExists(Username);

            if(user != null){
                return user;
            }else{
                return null;
            }
        }catch (Exception ex){
            return null;
        }
    }

    public int UsernameExist(String Username){
        try {
            User user=userRepository.UsernameExists(Username);

            if(user != null){
                return 1;
            }else{
                return 0;
            }
        }catch (Exception ex){
            return 0;
        }
    }

    public int CheckUsernameForEditUser(int id,String Username){
        try {
            User user=userRepository.CheckUsernameForEditUser(id,Username);

            if(user != null){
                return 1;
            }else{
                return 0;
            }
        }catch (Exception ex){
            return 0;
        }
    }



    public List<User> GetAllUsers() {
        List<User> users=userRepository.GetAllUsers();
        return users;
    }

    public User GetUserById(int id){
        try {
            User user=userRepository.GetUserById(id);
            if(user != null){
                return user;
            }else{
                return null;
            }
        }catch (Exception ex){
            return null;
        }
    }

    public boolean ChangePassword(int id, ChangePasswordBindingModel model){

        try {
            int status=userRepository.ChangePassword(id,model.oldpassword,model.newpassword);

            if(status==1){
                return true;
            }else{
                return false;
            }
        }catch (Exception ex){
            return false;
        }
    }
}
