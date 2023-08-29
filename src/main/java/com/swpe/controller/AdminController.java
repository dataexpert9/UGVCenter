package com.swpe.controller;


import com.alibaba.fastjson.JSONObject;
import com.swpe.BindingModels.*;
import com.swpe.entity.*;
import com.swpe.service.AdminService;
import com.swpe.service.LogService;
import com.swpe.service.UserService;
import com.swpe.util.Enumerators;
import com.swpe.util.JwtUtil;
import com.swpe.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("api/Admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;


    @RequestMapping(value = "/AddUser", produces = "application/json;charset=UTF-8")
    @PostMapping("/AddUser")
    @ResponseBody
    public CustomResponse AddUser(HttpServletRequest request, @RequestBody AddUserBindingModel bm) {
        try {
            String token = request.getHeader("Authorization");

            JSONObject loggedInUser = jwtUtil.getJson(token);

            if (loggedInUser != null) {

                if (Enumerators.RoleIntToEnum((Integer) loggedInUser.get("role")) == Enumerators.Roles.SuperAdmin) {


                    int Exists = userService.UsernameExist(bm.username);
                    if (Exists == 1)
                        return new CustomResponse("Username Already Exists", 401, null);
                    else {
                        int Status = adminService.AddUser(bm);

                        if (Status == 1) {
                            String LogText = loggedInUser.get("username")+" : "+Enumerators.RoleIntToEnum((Integer)loggedInUser.get("role")) + " Added new user :"+ bm.username+ ", with role +"+Enumerators.RoleIntToEnum(bm.role);
                            logService.CreateLog(new CreateLogBindingModel(Enumerators.ActionTypes.USER.ordinal(), Enumerators.SubActionTypes.CREATE.ordinal(), LogText));

                            return new CustomResponse("Success", 200, null);
                        }else
                            return new CustomResponse("Unhandled Exception Occured! Contact Administrator.", 401, null);
                    }
                } else
                    return new CustomResponse("You are not authorized to access this service.", 401, null);

            } else
                return new CustomResponse("Valid Token Is Required.", 401, null);

        } catch (Exception e) {
            e.printStackTrace();
            return new CustomResponse("Unhandled Exception Occured! Contact Administrator.", 401, null);
        }
    }

    @RequestMapping(value = "/AddUpdateUser", produces = "application/json;charset=UTF-8")
    @PostMapping("/AddUpdateUser")
    @ResponseBody
    public CustomResponse AddUpdateUser(HttpServletRequest request, @RequestBody AddUserBindingModel bm) {
        try {
            String token = request.getHeader("Authorization");

            JSONObject loggedInUser = jwtUtil.getJson(token);

            if (loggedInUser != null) {

                if (Enumerators.RoleIntToEnum((Integer) loggedInUser.get("role")) == Enumerators.Roles.SuperAdmin) {

                    if (bm.userid != 0) {
                        int Exists = userService.CheckUsernameForEditUser(bm.userid, bm.username);
                        if (Exists == 1)
                            return new CustomResponse("Username Already Exists", 401, null);
                        else {
                            int Status = adminService.UpdateUser(bm);
                            if (Status == 1) {

                                String LogText =  loggedInUser.get("username")+" : "+Enumerators.RoleIntToEnum((Integer)loggedInUser.get("role")) + " Updated user :"+ bm.username;
                                logService.CreateLog(new CreateLogBindingModel(Enumerators.ActionTypes.USER.ordinal(), Enumerators.SubActionTypes.UPDATE.ordinal(), LogText));

                                return new CustomResponse("Success", 200, null);
                            }else
                                return new CustomResponse("Unable To Update Try Again.", 401, null);
                        }
                    } else {
                        int Exists = userService.UsernameExist(bm.username);
                        if (Exists == 1)
                            return new CustomResponse("Username Already Exists", 401, null);
                        else {
                            int Status = adminService.AddUser(bm);

                            if (Status == 1){

                                String LogText =  loggedInUser.get("username")+" : "+Enumerators.RoleIntToEnum((Integer)loggedInUser.get("role")) + " created new user :"+ bm.username+", with role "+Enumerators.RoleIntToEnum(bm.role);
                                logService.CreateLog(new CreateLogBindingModel(Enumerators.ActionTypes.USER.ordinal(), Enumerators.SubActionTypes.CREATE.ordinal(), LogText));

                                return new CustomResponse("Success", 200, null);
                            }else
                                return new CustomResponse("Unhandled Exception Occured! Contact Administrator.", 401, null);
                        }
                    }
                } else
                    return new CustomResponse("You are not authorized to access this service.", 401, null);

            } else
                return new CustomResponse("Valid Token Is Required.", 401, null);

        } catch (Exception e) {
            e.printStackTrace();
            return new CustomResponse("Unhandled Exception Occured! Contact Administrator.", 401, null);
        }
    }

    @RequestMapping(value = "/DeleteEntity", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @GetMapping("/DeleteEntity")
    @ResponseBody
    public CustomResponse DeleteEntity(HttpServletRequest request, @RequestBody DeleteEntityBindingModel bm) {

        int Status = 0;

        String token = request.getHeader("Authorization");
        if (token.isEmpty() || token == null)
            return new CustomResponse("Token is missing.", 401, null);
        else {
            JSONObject loggedInUser = jwtUtil.getJson(token);

            if (loggedInUser == null)
                return new CustomResponse("Invalid Token.", 404, null);
            else {
                if (Enumerators.RoleIntToEnum((Integer) loggedInUser.get("role")) == Enumerators.Roles.SuperAdmin) {
                    Status = adminService.DeleteEntity(bm.id, bm.type);

                    if (Status == 1){

                        String LogText =  loggedInUser.get("username")+" : "+Enumerators.RoleIntToEnum((Integer)loggedInUser.get("role")) + " deleted :"+ Enumerators.EntityTypeIntToEnum(bm.type);
                        logService.CreateLog(new CreateLogBindingModel(Enumerators.ActionTypes.ADMIN.ordinal(), Enumerators.SubActionTypes.DELETE.ordinal(), LogText));

                        return new CustomResponse("Entity has been deleted successfully.", 200, null);
                    }else
                            return new CustomResponse("Either type is invalid or Error occured in server.", 500, null);
                    } else
                        return new CustomResponse("You are not authorized to access this service.", 401, null);
            }
        }
    }

    @RequestMapping(value = "/ResetPassword",produces = "application/json;charset=UTF-8")
    @PostMapping("/ResetPassword")
    @ResponseBody
    public CustomResponse ResetPassword(HttpServletRequest request,@RequestBody ResetPasswordBindingModel bmm) {
        try {
            String token=request.getHeader("Authorization");

            if(token=="" || token==null)
                return new CustomResponse("Token is required.", 401, null);



            JSONObject jsonObject=jwtUtil.getJson(token);

            if(jsonObject != null) {
                if (Enumerators.RoleIntToEnum((Integer) jsonObject.get("role")) == Enumerators.Roles.SuperAdmin) {

                    int userid = (Integer) jsonObject.get("uniqueid");
                    int status = adminService.ResetPassword(bmm.userid);
                    if (status == 1) {

                        User user=userService.GetUserById(userid);

                        if(user ==null)
                            return new CustomResponse("User with id doesn't exist.", 401, null);

                        String LogText =  jsonObject.get("username")+" : "+Enumerators.RoleIntToEnum((Integer)jsonObject.get("role")) + " reset password of :"+user.getUserName();
                        logService.CreateLog(new CreateLogBindingModel(Enumerators.ActionTypes.USER.ordinal(), Enumerators.SubActionTypes.UPDATE.ordinal(), LogText));

                        return new CustomResponse("Password Has Been Reset Successfully.", 200, null);
                    } else {
                        return new CustomResponse("Internal Server Error.", 500, null);
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

    @RequestMapping(value = "/GetAllUGV",produces = "application/json;charset=UTF-8")
    @GetMapping("/GetAllUGV")
    @ResponseBody
    public CustomResponse GetAllUGV(HttpServletRequest request){
        try {
            String token = request.getHeader("Authorization");
            if (token.isEmpty() || token == null)
                return new CustomResponse("You are not authorized to access this service.", 401, null);
            else {
                JSONObject loggedInUser = jwtUtil.getJson(token);

                if (loggedInUser == null)
                    return new CustomResponse("Invalid Token.", 404, null);
                else {
                    if (Enumerators.RoleIntToEnum((Integer) loggedInUser.get("role")) == Enumerators.Roles.SuperAdmin) {
                        List<ugvs> ugvs = adminService.GetAllUGV();

                        String LogText =  loggedInUser.get("username")+" : "+Enumerators.RoleIntToEnum((Integer)loggedInUser.get("role")) + " get list of all UGVs";
                        logService.CreateLog(new CreateLogBindingModel(Enumerators.ActionTypes.UGV.ordinal(), Enumerators.SubActionTypes.GETALL.ordinal(), LogText));


                        return new CustomResponse("Success", 200, ugvs);
                    } else
                        return new CustomResponse("You are not authorized to access this service.", 401, null);
                }
            }
        }catch (Exception ex){
            return new CustomResponse("Internal Server Error.", 500, null);
        }
    }

    @RequestMapping(value = "/AddUpdateUGV", produces = "application/json;charset=UTF-8")
    @PostMapping("/AddUpdateUGV")
    @ResponseBody
    public CustomResponse AddUpdateUGV(HttpServletRequest request, @RequestBody UGVBindingModel bm) {
        try {
            String token = request.getHeader("Authorization");

            JSONObject loggedInUser = jwtUtil.getJson(token);

            if (loggedInUser != null) {

                if (Enumerators.RoleIntToEnum((Integer) loggedInUser.get("role")) == Enumerators.Roles.SuperAdmin) {

                    if (bm.ugvid != 0) {
                        ugvs ugv = adminService.CheckUgvAlreadyExistForUpdate(bm.ugvid,bm.name);
                        if (ugv != null)
                            return new CustomResponse("UGV With This Name Already Exists", 401, null);
                        else {
                            int Status = adminService.UpdateUGV(bm);
                            if (Status == 1){

                                String LogText = loggedInUser.get("username")+" : "+Enumerators.RoleIntToEnum((Integer)loggedInUser.get("role"))+ " update ugv : "+bm.name+" with IP :"+bm.ip;
                                logService.CreateLog(new CreateLogBindingModel(Enumerators.ActionTypes.UGV.ordinal(), Enumerators.SubActionTypes.UPDATE.ordinal(), LogText));

                                return new CustomResponse("Success", 200, null);
                            }else
                                return new CustomResponse("Unable To Update Try Again.", 401, null);
                            }
                    } else {
                        ugvs ugv = adminService.CheckUgvAlreadyExist(bm.name);
                        if (ugv != null)
                            return new CustomResponse("UGV Already Exists", 401, null);
                        else {
                            int Status = adminService.AddUGV(bm);

                            if (Status == 1){

                                String LogText = loggedInUser.get("username")+" : "+Enumerators.RoleIntToEnum((Integer)loggedInUser.get("role")) + " added new  ugv : "+bm.name+" with IP :"+bm.ip;
                                logService.CreateLog(new CreateLogBindingModel(Enumerators.ActionTypes.UGV.ordinal(), Enumerators.SubActionTypes.CREATE.ordinal(), LogText));

                                return new CustomResponse("Success", 200, null);
                            }else
                                return new CustomResponse("Unhandled Exception Occured! Contact Administrator.", 401, null);
                        }
                    }
                } else
                    return new CustomResponse("You are not authorized to access this service.", 401, null);

            } else
                return new CustomResponse("Valid Token Is Required.", 401, null);

        } catch (Exception e) {
            e.printStackTrace();
            return new CustomResponse("Unhandled Exception Occured! Contact Administrator.", 401, null);
        }
    }

    @RequestMapping(value = "/GetUGVById",produces = "application/json;charset=UTF-8")
    @GetMapping("/GetUGVById")
    @ResponseBody
    public CustomResponse GetUGVById(HttpServletRequest request,int ugvid) {

        String token = request.getHeader("Authorization");

        JSONObject loggedInUser = jwtUtil.getJson(token);

        if (loggedInUser != null) {

            if (Enumerators.RoleIntToEnum((Integer) loggedInUser.get("role")) == Enumerators.Roles.SuperAdmin) {

                ugvs ugv;
                if (ugvid != 0) {
                    ugv = adminService.GetUGVById(ugvid);

                    String LogText = loggedInUser.get("username")+" : "+Enumerators.RoleIntToEnum((Integer)loggedInUser.get("role")) + " get UGV by id : " + ugv.getName() + " with IP :" + ugv.getIp();
                    logService.CreateLog(new CreateLogBindingModel(Enumerators.ActionTypes.UGV.ordinal(), Enumerators.SubActionTypes.GETBYID.ordinal(), LogText));

                    return new CustomResponse("Success", 200, ugv);
                } else {
                    return new CustomResponse("user id is required.", 400, null);
                }
            } else {
                return new CustomResponse("You are not authorized to access this service.", 401, null);
            }
        }else
            return new CustomResponse("Valid Token Is Required.", 401, null);
    }

    // map services from here
    @RequestMapping(value = "/GetMapById",produces = "application/json;charset=UTF-8")
    @GetMapping("/GetMapById")
    @ResponseBody
    public CustomResponse GetMapById(HttpServletRequest request,int mapid) {

        String token = request.getHeader("Authorization");

        JSONObject loggedInUser = jwtUtil.getJson(token);

        if (loggedInUser != null) {

            if (Enumerators.RoleIntToEnum((Integer) loggedInUser.get("role")) == Enumerators.Roles.SuperAdmin) {

                Map map;
                if (mapid != 0) {
                    map = adminService.GetMapById(mapid);

                    String LogText = loggedInUser.get("username")+" : "+Enumerators.RoleIntToEnum((Integer)loggedInUser.get("role")) + " get map name : " + map.getName();
                    logService.CreateLog(new CreateLogBindingModel(Enumerators.ActionTypes.MAP.ordinal(), Enumerators.SubActionTypes.GETBYID.ordinal(), LogText));

                    return new CustomResponse("Success", 200, map);
                } else {
                    return new CustomResponse("user id is required.", 400, null);
                }
            } else
                return new CustomResponse("You are not authorized to access this service.", 401, null);

        } else
            return new CustomResponse("Valid Token Is Required.", 401, null);
    }

    @RequestMapping(value = "/GetAllMaps",produces = "application/json;charset=UTF-8")
    @GetMapping("/GetAllMaps")
    @ResponseBody
    public CustomResponse GetAllMaps(HttpServletRequest request){
        try {
            String token = request.getHeader("Authorization");
            if (token.isEmpty() || token == null)
                return new CustomResponse("You are not authorized to access this service.", 401, null);
            else {
                JSONObject loggedInUser = jwtUtil.getJson(token);

                if (loggedInUser == null)
                    return new CustomResponse("Invalid Token.", 404, null);
                else {
                    if (Enumerators.RoleIntToEnum((Integer) loggedInUser.get("role")) == Enumerators.Roles.SuperAdmin) {
                        List<Map> maps = adminService.GetAllMaps();

                        String LogText = loggedInUser.get("username")+" : "+Enumerators.RoleIntToEnum((Integer)loggedInUser.get("role")) + " Get list of all maps ";
                        logService.CreateLog(new CreateLogBindingModel(Enumerators.ActionTypes.MAP.ordinal(), Enumerators.SubActionTypes.GETALL.ordinal(), LogText));

                        return new CustomResponse("Success", 200, maps);
                    } else
                        return new CustomResponse("You are not authorized to access this service.", 401, null);
                }
            }
        }catch (Exception ex){
            return new CustomResponse("Internal Server Error.", 500, null);
        }
    }

    @RequestMapping(value = "/AddUpdateMap", produces = "application/json;charset=UTF-8")
    @PostMapping("/AddUpdateMap")
    @ResponseBody
    public CustomResponse AddUpdateMap(HttpServletRequest request, @RequestBody MapBindingModel bm) {
        try {
            String token = request.getHeader("Authorization");

            JSONObject loggedInUser = jwtUtil.getJson(token);

            if (loggedInUser != null) {

                if (Enumerators.RoleIntToEnum((Integer) loggedInUser.get("role")) == Enumerators.Roles.SuperAdmin) {

                    if (bm.mapid != 0) {
                        Map map = adminService.CheckMapAlreadyExistForUpdate(bm.mapid,bm.name);
                        if (map != null)
                            return new CustomResponse("Map With This Name Already Exists", 401, null);
                        else {
                            map=adminService.CheckMapAlreadyExistById(bm.mapid);
                            Utility util=new Utility();

                            if(!map.getFilepath().equals(bm.filepath))
                                map.setFilepath(bm.filepath);


                            map=util.ReadYamlAndMap(map);

                            if(map==null)
                                return new CustomResponse("File Doesnot Exist On Mentioned Path.", 401, null);

                            map.setName(bm.name);
                            map.setFilepath(bm.filepath);
                            map.setDetails(bm.details);

                            int Status = adminService.UpdateMap(map);
                            if (Status == 1) {

                                String LogText = loggedInUser.get("username")+" : "+Enumerators.RoleIntToEnum((Integer)loggedInUser.get("role")) + " updated map with name : "+map.getName();
                                logService.CreateLog(new CreateLogBindingModel(Enumerators.ActionTypes.MAP.ordinal(), Enumerators.SubActionTypes.UPDATE.ordinal(), LogText));

                                return new CustomResponse("Success", 200, null);
                            }else
                                return new CustomResponse("Unable To Update Try Again.", 401, null);
                        }
                    } else {
                        Map map = adminService.CheckMapAlreadyExist(bm.name);
                        if (map != null)
                            return new CustomResponse("Map Already Exists", 401, null);
                        else {
                            int Status=0;
                            map=adminService.CheckMapAlreadyExist(bm.name);

                            if(map==null)
                                Status=adminService.AddMap(bm);

                            map=adminService.CheckMapAlreadyExist(bm.name);

                            Utility util=new Utility();
                            map=util.ReadYamlAndMap(map);

                            if(map==null)
                                return new CustomResponse("File Doesnot Exist On Mentioned Path.", 401, null);

                            if(Status==0)
                                Status = adminService.AddMap(map);

                            if (Status == 1) {

                                String LogText = loggedInUser.get("username")+" : "+Enumerators.RoleIntToEnum((Integer)loggedInUser.get("role")) + " created new map with name : "+map.getName();
                                logService.CreateLog(new CreateLogBindingModel(Enumerators.ActionTypes.MAP.ordinal(), Enumerators.SubActionTypes.CREATE.ordinal(), LogText));

                                return new CustomResponse("Success", 200, null);
                            }else
                                return new CustomResponse("Unhandled Exception Occured! Contact Administrator.", 401, null);
                        }
                    }
                } else
                    return new CustomResponse("You are not authorized to access this service.", 401, null);

            } else
                return new CustomResponse("Valid Token Is Required.", 401, null);

        } catch (Exception e) {
            e.printStackTrace();
            return new CustomResponse("Unhandled Exception Occured! Contact Administrator.", 401, null);
        }
    }


    @RequestMapping(value = "/GetAllLogs",produces = "application/json;charset=UTF-8")
    @GetMapping("/GetAllLogs")
    @ResponseBody
    public CustomResponse GetAllLogs(HttpServletRequest request){
        try {
            String token = request.getHeader("Authorization");
            if (token.isEmpty() || token == null)
                return new CustomResponse("You are not authorized to access this service.", 401, null);
            else {
                JSONObject loggedInUser = jwtUtil.getJson(token);

                if (loggedInUser == null)
                    return new CustomResponse("Invalid Token.", 404, null);
                else {
                    if (Enumerators.RoleIntToEnum((Integer) loggedInUser.get("role")) == Enumerators.Roles.SuperAdmin) {
                        List<logs> logs = logService.GetAllLogs();

                        String LogText = loggedInUser.get("username")+" : "+Enumerators.RoleIntToEnum((Integer)loggedInUser.get("role")) + " get list of all logs";
                        logService.CreateLog(new CreateLogBindingModel(Enumerators.ActionTypes.LOG.ordinal(), Enumerators.SubActionTypes.GETALL.ordinal(), LogText));

                        return new CustomResponse("Success", 200, logs);
                    } else
                        return new CustomResponse("You are not authorized to access this service.", 401, null);
                }
            }
        }catch (Exception ex){
            return new CustomResponse("Internal Server Error.", 500, null);
        }
    }



}