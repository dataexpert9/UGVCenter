package com.swpe.controller;

import com.alibaba.fastjson.JSONObject;
import com.swpe.BindingModels.ChangePasswordBindingModel;
import com.swpe.BindingModels.CreateLogBindingModel;
import com.swpe.BindingModels.LoginBindingModel;
import com.swpe.entity.CustomResponse;
import com.swpe.entity.User;
import com.swpe.service.LogService;
import com.swpe.service.UserService;
import com.swpe.util.Enumerators;
import com.swpe.util.JwtUtil;
import com.swpe.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.text.ParseException;
import java.util.List;
import com.swpe.util.Enumerators.*;

@Controller
@RequestMapping("api/User")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;

    @Autowired
    private JwtUtil jwtUtil;


    @RequestMapping(value = "/Login",produces = "application/json;charset=UTF-8")
    @PostMapping("/Login")
    @ResponseBody
    public CustomResponse Login(@RequestBody LoginBindingModel bm) {
        try {

            bm.password= MD5.getMD5(bm.password);
            User user = userService.Login(bm);

            if (user != null) {
                String tokensub = jwtUtil.generalSubject(user.getId(),user.getUserName(), user.getFullName(), user.getRole());
                user.setToken(jwtUtil.createJWT("jwt", tokensub, 24 * 60 * 60 * 1000 * 3));

                String LogText=user.getUserName()+" logged into system ";
                logService.CreateLog(new CreateLogBindingModel(ActionTypes.USER.ordinal(),SubActionTypes.LOGIN.ordinal(),LogText));


                return new CustomResponse("Success", 200, user);
            } else {

                return new CustomResponse("Invalid Username or Password.", 401,null);
            }
        }catch(ParseException ex){
            return new CustomResponse("Exception occured! contact Administrator", 401, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CustomResponse("Unhandled Exception Occured! Contact Administrator.", 401, null);

    }

    @RequestMapping(value = "/GetAllUsers",produces = "application/json;charset=UTF-8")
    @GetMapping("/GetAllUsers")
    @ResponseBody
    public CustomResponse GetAllUsers(HttpServletRequest request){

        String token=request.getHeader("Authorization");
        if(token.isEmpty() || token == null)
            return new CustomResponse("You are not authorized to access this service.",401,null);
        else{
            JSONObject loggedInUser=jwtUtil.getJson(token);

            if(loggedInUser == null)
                return new CustomResponse("Invalid Token.", 404, null);
            else{
                if(Enumerators.RoleIntToEnum((Integer)loggedInUser.get("role"))== Roles.SuperAdmin){
                    List<User> users=userService.GetAllUsers();

                    String LogText=loggedInUser.get("username")+" : "+Enumerators.RoleIntToEnum((Integer)loggedInUser.get("role")) +" Get all user list ";
                    logService.CreateLog(new CreateLogBindingModel(ActionTypes.USER.ordinal(),SubActionTypes.GETALL.ordinal(),LogText));


                    return new CustomResponse("Success", 200, users);
                }else
                    return new CustomResponse("You are not authorized to access this service.",401,null);
            }
         }
    }

    @RequestMapping(value = "/GetUserById",produces = "application/json;charset=UTF-8")
    @GetMapping("/GetUserById")
    @ResponseBody
    public CustomResponse GetUserById(HttpServletRequest request,int userid) {

        String token = request.getHeader("Authorization");
        if (token.isEmpty() || token == null)
            return new CustomResponse("You are not authorized to access this service.", 401, null);
        else {
            JSONObject loggedInUser = jwtUtil.getJson(token);

            if (loggedInUser == null)
                return new CustomResponse("Invalid Token.", 404, null);
            else {
                if (Enumerators.RoleIntToEnum((Integer) loggedInUser.get("role")) == Roles.SuperAdmin) {

                    User user;
                    if (userid != 0) {
                        user = userService.GetUserById(userid);

                        String LogText = loggedInUser.get("username")+" : "+Enumerators.RoleIntToEnum((Integer)loggedInUser.get("role")) + " Get all user list ";
                        logService.CreateLog(new CreateLogBindingModel(ActionTypes.USER.ordinal(), SubActionTypes.GETALL.ordinal(), LogText));

                        if(user.getToken()!=null || user.getToken()!="")
                            user.setToken("");
                        return new CustomResponse("Success", 200, user);
                    } else {
                        return new CustomResponse("user id is required.", 400, null);
                    }
                } else {
                    return new CustomResponse("You are not authorized to access this service.", 401, null);
                }
            }
        }
    }

    @RequestMapping(value = "/ChangePassword",produces = "application/json;charset=UTF-8")
    @PostMapping("/ChangePassword")
    @ResponseBody
    public CustomResponse ChangePassword(HttpServletRequest request,@RequestBody ChangePasswordBindingModel bmm) {
        try {
            String token=request.getHeader("Authorization");

            JSONObject jsonObject=jwtUtil.getJson(token);

            if(jsonObject != null){

                int userid=(Integer)jsonObject.get("uniqueid");

                bmm.oldpassword=MD5.getMD5(bmm.oldpassword);
                bmm.newpassword=MD5.getMD5(bmm.newpassword);

                boolean status = userService.ChangePassword(userid,bmm);
                if(status){

                    String LogText = jsonObject.get("username")+" : "+Enumerators.RoleIntToEnum((Integer)jsonObject.get("role")) + " changed password ";
                    logService.CreateLog(new CreateLogBindingModel(ActionTypes.USER.ordinal(), SubActionTypes.UPDATE.ordinal(), LogText));

                    return new CustomResponse("Password Has Been Changed Successfully.", 200,null);
                }else{
                    return new CustomResponse("Invalid Old Password.", 404,null);
                }

            }else{
                return new CustomResponse("You are not authorized to access this service.",401,null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new CustomResponse("Unhandled Exception Occured! Contact Administrator.", 401, null);
        }
    }

    @RequestMapping(value = "/test", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String test(HttpServletResponse response, HttpServletRequest request) {
        response.setHeader("Access-Control-Allow-Origin", "*"); //Access-Control-Allow
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");

        String sjson = request.getParameter("json");
        JSONObject ret = new JSONObject();

        ret=userService.test(sjson);

        return ret.toJSONString();
    }




}
