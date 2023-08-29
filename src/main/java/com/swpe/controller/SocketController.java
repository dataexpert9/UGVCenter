package com.swpe.controller;


import com.alibaba.fastjson.JSONObject;
import com.swpe.BindingModels.*;
import com.swpe.entity.CustomResponse;
import com.swpe.entity.Map;
import com.swpe.entity.ugvs;
import com.swpe.service.AdminService;
import com.swpe.service.LogService;
import com.swpe.util.Enumerators;
import com.swpe.util.JwtUtil;
import com.swpe.util.ThreadedSocket;
import com.swpe.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("api/Socket")
@CrossOrigin(origins = "*")
public class SocketController {
    ThreadedSocket handler;

    public SocketController(){
        handler=new ThreadedSocket();
    }

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AdminService adminService;


    @Autowired
    private LogService logService;



    @RequestMapping(value = "/CreateConnection",produces = "application/json;charset=UTF-8")
    @GetMapping("/CreateConnection")
    @ResponseBody
    public CustomResponse CreateConnection(HttpServletRequest request,int ugvid) {

        String token = request.getHeader("Authorization");
        if (token.isEmpty() || token == null)
            return new CustomResponse("You are not authorized to access this service.", 401, null);
        else {
            JSONObject loggedInUser = jwtUtil.getJson(token);

            if (loggedInUser == null)
                return new CustomResponse("Invalid Token.", 404, null);
            else {
                if (Enumerators.RoleIntToEnum((Integer) loggedInUser.get("role")) == Enumerators.Roles.SuperAdmin) {


                    ugvs ugv = adminService.CheckUgvAlreadyExistById(ugvid);
                    if (ugv == null)
                        return new CustomResponse("UGV With Id Doesn't Exists", 401, null);

                    if (handler.CreateConnection(ugv.getid(), ugv.getName(), ugv.getIp(), ugv.getPort())) {

                        String LogText = loggedInUser.get("username") + " : " + Enumerators.RoleIntToEnum((Integer) loggedInUser.get("role")) + " Created connection with UGV : "+ugv.getName() +" and IP : "+ugv.getIp();
                        logService.CreateLog(new CreateLogBindingModel(Enumerators.ActionTypes.USER.ordinal(), Enumerators.SubActionTypes.UPDATE.ordinal(), LogText));

                        return new CustomResponse("Success", 200, null);
                    } else
                        return new CustomResponse("Failed To Create Socket. Try Again To Try Updating Ip Address", 401, null);
                } else {
                    return new CustomResponse("You are not authorized to access this service.", 401, null);
                }
            }
        }
    }

    @RequestMapping(value = "/CloseConnection",produces = "application/json;charset=UTF-8")
    @GetMapping("/CloseConnection")
    @ResponseBody
    public CustomResponse CloseConnection(HttpServletRequest request,int ugvid){

        String token = request.getHeader("Authorization");
        if (token.isEmpty() || token == null)
            return new CustomResponse("You are not authorized to access this service.", 401, null);
        else {
            JSONObject loggedInUser = jwtUtil.getJson(token);

            if (loggedInUser == null)
                return new CustomResponse("Invalid Token.", 404, null);
            else {
                if (Enumerators.RoleIntToEnum((Integer) loggedInUser.get("role")) == Enumerators.Roles.SuperAdmin) {


                    ugvs ugv = adminService.CheckUgvAlreadyExistById(ugvid);
                    if (ugv == null)
                        return new CustomResponse("UGV With Id Doesn't Exists", 401, null);

                    if (handler.CloseConnection(ugv.getid())) {

                        String LogText = loggedInUser.get("username") + " : " + Enumerators.RoleIntToEnum((Integer) loggedInUser.get("role")) + " closed connection with UGV : "+ugv.getName() +" and IP : "+ugv.getIp();
                        logService.CreateLog(new CreateLogBindingModel(Enumerators.ActionTypes.USER.ordinal(), Enumerators.SubActionTypes.UPDATE.ordinal(), LogText));

                        return new CustomResponse("Success", 200, null);
                    }else
                        return new CustomResponse("Failed To Close Socket. Try Again.", 401, null);

                } else
                    return new CustomResponse("You are not authorized to access this service.", 401, null);
            }
        }
    }

    @RequestMapping(value = "/SelectMap",produces = "application/json;charset=UTF-8")
    @PostMapping("/SelectMap")
    @ResponseBody
    public CustomResponse SelectMap(HttpServletRequest request, @RequestBody SelectMapBindingModel bm) {
        try {
            String token=request.getHeader("Authorization");

            if(token=="" || token==null)
                return new CustomResponse("Token is required.", 401, null);


            JSONObject jsonObject=jwtUtil.getJson(token);

            if(jsonObject != null) {
                if (Enumerators.RoleIntToEnum((Integer) jsonObject.get("role")) == Enumerators.Roles.SuperAdmin) {

                    if(bm.ugvids.length>0) {
                        for (int i = 0; i < bm.ugvids.length; i++) {
                            int ugvid = bm.ugvids[i];

                            ugvs ugv=adminService.CheckUgvAlreadyExistById(ugvid);
                            Map map=adminService.CheckMapAlreadyExistById(bm.mapid);

                            if(map==null || ugv==null)
                                return new CustomResponse("Map or UGV not found.", 401, null);

                            if(Utility.SelectMap(ugv.getid(),map.getName())) {
                                String LogText = jsonObject.get("username") + " : " + Enumerators.RoleIntToEnum((Integer) jsonObject.get("role")) + " selected map : " + map.getName() + " on UGV : " + ugv.getName() + " and IP : " + ugv.getIp();
                                logService.CreateLog(new CreateLogBindingModel(Enumerators.ActionTypes.SOCKET.ordinal(), Enumerators.SubActionTypes.GETBYID.ordinal(), LogText));
                            }else
                                return new CustomResponse("Internal Server Error.", 500, null);
                        }
                    }else
                        return new CustomResponse("One or more ugv ids required.", 401, null);

                    return new CustomResponse("Map has been selected successfully.", 200, null);

                } else
                    return new CustomResponse("You are not authorized to access this service.", 401, null);
            }else
                return new CustomResponse("Invalid Token.", 401, null);

        } catch (Exception e) {
            e.printStackTrace();
            return new CustomResponse("Unhandled Exception Occured! Contact Administrator.", 401, null);
        }
    }


    @RequestMapping(value = "/SwitchToManualControl",produces = "application/json;charset=UTF-8")
    @PostMapping("/SwitchToManualControl")
    @ResponseBody
    public CustomResponse SwitchToManualControl(HttpServletRequest request, @RequestBody ManualControlBindingModel bm) {

        try {
            String token=request.getHeader("Authorization");

            if(token=="" || token==null)
                return new CustomResponse("Token is required.", 401, null);


            JSONObject jsonObject=jwtUtil.getJson(token);

            if(jsonObject != null) {
                if (Enumerators.RoleIntToEnum((Integer) jsonObject.get("role")) == Enumerators.Roles.SuperAdmin) {

                    ugvs ugv=adminService.CheckUgvAlreadyExistById(bm.ugvid);
                    if(ugv==null)
                        return new CustomResponse("UGV not found.", 401, null);
                    if(bm.SwitchToManual==0 || bm.SwitchToManual==1) {
                        if (Utility.ManualControl(bm.SwitchToManual,ugv.getid())) {

                            String LogText = jsonObject.get("username") + " : " + Enumerators.RoleIntToEnum((Integer) jsonObject.get("role")) + " set UGV "+ugv.getName() +" and IP : "+ugv.getIp()+" to manual control";
                            logService.CreateLog(new CreateLogBindingModel(Enumerators.ActionTypes.SOCKET.ordinal(), Enumerators.SubActionTypes.UPDATE.ordinal(), LogText));

                            return new CustomResponse("Car has been shifted to manual control successfully.", 200, null);
                        } else {
                            return new CustomResponse("Internal Server Error. Try Again", 500, null);
                        }
                    }else{
                        return new CustomResponse("Invalid Switch To Manual Value.", 401, null);
                    }

                } else
                    return new CustomResponse("You are not authorized to access this service.", 401, null);
            }else
                return new CustomResponse("Invalid Token.", 401, null);

        } catch (Exception e) {
            e.printStackTrace();
            return new CustomResponse("Unhandled Exception Occured! Contact Administrator.", 401, null);
        }
    }



    @RequestMapping(value = "/SendOrderForManualControl",produces = "application/json;charset=UTF-8")
    @PostMapping("/SendOrderForManualControl")
    @ResponseBody
    public CustomResponse SendOrderForManualControl(HttpServletRequest request, @RequestBody SendOrderBindingModel bm) {
        try {
            String token=request.getHeader("Authorization");

            if(token=="" || token==null)
                return new CustomResponse("Token is required.", 401, null);


            JSONObject jsonObject=jwtUtil.getJson(token);

            if(jsonObject != null) {
                if (Enumerators.RoleIntToEnum((Integer) jsonObject.get("role")) == Enumerators.Roles.SuperAdmin) {

                    ugvs ugv=adminService.CheckUgvAlreadyExistById(bm.ugvid);
                    if(ugv==null)
                        return new CustomResponse("UGV not found.", 401, null);
                    if(bm.OrderType>=0 && bm.OrderType<=6) {
                        if (Utility.SendOrderForManualControl(bm.OrderType,ugv.getid())) {

                            String LogText = jsonObject.get("username") + " : " + Enumerators.RoleIntToEnum((Integer) jsonObject.get("role")) + " moved car with order type "+bm.OrderType+" on UGV "+ugv.getName() +" and IP : "+ugv.getIp()+" to manual control";
                            logService.CreateLog(new CreateLogBindingModel(Enumerators.ActionTypes.SOCKET.ordinal(), Enumerators.SubActionTypes.UPDATE.ordinal(), LogText));

                            return new CustomResponse("Order has been sent successfully.", 200, null);
                        } else {
                            return new CustomResponse("Internal Server Error. Try Again", 500, null);
                        }
                    }else{
                        return new CustomResponse("Invalid send order values.", 401, null);
                    }

                } else
                    return new CustomResponse("You are not authorized to access this service.", 401, null);
            }else
                return new CustomResponse("Invalid Token.", 401, null);

        } catch (Exception e) {
            e.printStackTrace();
            return new CustomResponse("Unhandled Exception Occured! Contact Administrator.", 401, null);
        }
    }

    @RequestMapping(value = "/SetGlobalPath",produces = "application/json;charset=UTF-8")
    @PostMapping("/SetGlobalPath")
    @ResponseBody
    public CustomResponse SetGlobalPath(HttpServletRequest request, @RequestBody GlobalPathBindingModel bm) {
        try {
            String token=request.getHeader("Authorization");

            if(token=="" || token==null)
                return new CustomResponse("Token is required.", 401, null);


            JSONObject jsonObject=jwtUtil.getJson(token);

            if(jsonObject != null) {
                if (Enumerators.RoleIntToEnum((Integer) jsonObject.get("role")) == Enumerators.Roles.SuperAdmin) {

                    ugvs ugv=adminService.CheckUgvAlreadyExistById(bm.ugvid);
                    if(ugv==null)
                        return new CustomResponse("UGV not found.", 401, null);
                    if(bm.x!=0 && bm.y!=0) {
                        if (Utility.SetGlobalPath(bm.x,bm.y,ugv.getid())) {

                            String LogText = jsonObject.get("username") + " : " + Enumerators.RoleIntToEnum((Integer) jsonObject.get("role")) + "set global path x:"+bm.x+" & y:"+bm.y+" on UGV "+ugv.getName() +" and IP : "+ugv.getIp()+" to manual control";
                            logService.CreateLog(new CreateLogBindingModel(Enumerators.ActionTypes.SOCKET.ordinal(), Enumerators.SubActionTypes.UPDATE.ordinal(), LogText));



                            return new CustomResponse("Order has been sent successfully.", 200, null);
                        } else {
                            return new CustomResponse("Internal Server Error. Try Again", 500, null);
                        }
                    }else{
                        return new CustomResponse("Invalid target points.", 401, null);
                    }

                } else
                    return new CustomResponse("You are not authorized to access this service.", 401, null);
            }else
                return new CustomResponse("Invalid Token.", 401, null);

        } catch (Exception e) {
            e.printStackTrace();
            return new CustomResponse("Unhandled Exception Occured! Contact Administrator.", 401, null);
        }
    }

    @RequestMapping(value = "/StartMoving",produces = "application/json;charset=UTF-8")
    @PostMapping("/StartMoving")
    @ResponseBody
    public CustomResponse StartMoving(HttpServletRequest request, @RequestBody int ugvid) {
        try {
            String token=request.getHeader("Authorization");

            if(token=="" || token==null)
                return new CustomResponse("Token is required.", 401, null);


            JSONObject jsonObject=jwtUtil.getJson(token);

            if(jsonObject != null) {
                if (Enumerators.RoleIntToEnum((Integer) jsonObject.get("role")) == Enumerators.Roles.SuperAdmin) {

                    ugvs ugv=adminService.CheckUgvAlreadyExistById(ugvid);
                    if(ugv==null)
                        return new CustomResponse("UGV not found.", 401, null);

                    if (Utility.StartMoving(ugv.getid())) {

                        String LogText = jsonObject.get("username") + " : " + Enumerators.RoleIntToEnum((Integer) jsonObject.get("role")) + " send order to start moving on UGV "+ugv.getName() +" and IP : "+ugv.getIp()+" to manual control";
                        logService.CreateLog(new CreateLogBindingModel(Enumerators.ActionTypes.SOCKET.ordinal(), Enumerators.SubActionTypes.UPDATE.ordinal(), LogText));



                        return new CustomResponse("Order has been sent successfully.", 200, null);
                    } else {
                        return new CustomResponse("Internal Server Error. Try Again", 500, null);
                    }


                } else
                    return new CustomResponse("You are not authorized to access this service.", 401, null);
            }else
                return new CustomResponse("Invalid Token.", 401, null);

        } catch (Exception e) {
            e.printStackTrace();
            return new CustomResponse("Unhandled Exception Occured! Contact Administrator.", 401, null);
        }
    }

    @RequestMapping(value = "/StopMoving",produces = "application/json;charset=UTF-8")
    @PostMapping("/StopMoving")
    @ResponseBody
    public CustomResponse StopMoving(HttpServletRequest request, @RequestBody int ugvid) {
        try {
            String token=request.getHeader("Authorization");

            if(token=="" || token==null)
                return new CustomResponse("Token is required.", 401, null);


            JSONObject jsonObject=jwtUtil.getJson(token);

            if(jsonObject != null) {
                if (Enumerators.RoleIntToEnum((Integer) jsonObject.get("role")) == Enumerators.Roles.SuperAdmin) {

                    ugvs ugv=adminService.CheckUgvAlreadyExistById(ugvid);
                    if(ugv==null)
                        return new CustomResponse("UGV not found.", 401, null);

                    if (Utility.StopMoving(ugv.getid())) {

                        String LogText = jsonObject.get("username") + " : " + Enumerators.RoleIntToEnum((Integer) jsonObject.get("role")) + " send order to stop moving on UGV "+ugv.getName() +" and IP : "+ugv.getIp()+" to manual control";
                        logService.CreateLog(new CreateLogBindingModel(Enumerators.ActionTypes.SOCKET.ordinal(), Enumerators.SubActionTypes.UPDATE.ordinal(), LogText));

                        return new CustomResponse("Order has been sent successfully.", 200, null);
                    } else {
                        return new CustomResponse("Internal Server Error. Try Again", 500, null);
                    }

                } else
                    return new CustomResponse("You are not authorized to access this service.", 401, null);
            }else
                return new CustomResponse("Invalid Token.", 401, null);

        } catch (Exception e) {
            e.printStackTrace();
            return new CustomResponse("Unhandled Exception Occured! Contact Administrator.", 401, null);
        }
    }


}
