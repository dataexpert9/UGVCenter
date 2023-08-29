package com.swpe.service.impl;

import com.swpe.BindingModels.AddUserBindingModel;
import com.swpe.BindingModels.DeleteEntityBindingModel;
import com.swpe.BindingModels.MapBindingModel;
import com.swpe.BindingModels.UGVBindingModel;
import com.swpe.dao.AdminRepository;
import com.swpe.dao.MapRepository;
import com.swpe.dao.UGVRepository;
import com.swpe.entity.Map;
import com.swpe.entity.User;
import com.swpe.entity.ugvs;
import com.swpe.service.AdminService;
import com.swpe.util.JwtUtil;
import com.swpe.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UGVRepository ugvRepository;

    @Autowired
    private MapRepository mapRepository;


    public int AddUser(AddUserBindingModel bm){
        try {
            String password= MD5.getMD5("123456");
            int Status = adminRepository.AddUser(bm.username, bm.role, bm.name, password, 0);
            return Status;
        }catch (Exception ex){
            return 0;
        }
    }
    public int UpdateUser(AddUserBindingModel bm){
        try {
//            String password= MD5.getMD5("123456");
            int Status = adminRepository.UpdateUser(bm.userid,bm.username, bm.role, bm.name);
            return Status;
        }catch (Exception ex){
            return 0;
        }
    }

    public int DeleteEntity(int id,int type){

        try{
            int Status;
            switch (type)
            {
                case 0:// for user
                    Status=adminRepository.DeleteUser(id);
                    break;
                case 1:// for ugv
                    Status=adminRepository.DeleteUGV(id);
                    break;
                case 2:// for map
                    Status=adminRepository.DeleteMap(id);
                    break;
                default:
                    Status=0;
                    break;
            }

            if(Status==1)
                return 1;
            else
                return 0;
        }catch (Exception ex){
            return 0;
        }
    }

    public int ResetPassword(int userid){
        try{
            String password=MD5.getMD5("123456");
            int Status=adminRepository.ResetPassword(userid,password);

            if(Status==1)
                return 1;
            else
                return 0;
        }catch (Exception ex){
            return 0;
        }
    }

    public List<ugvs> GetAllUGV(){
        try {
            List<ugvs> ugvs = ugvRepository.GetAllUGV();
            return ugvs;
        }catch (Exception ex){
            return null;
        }
    }

    public ugvs CheckUgvAlreadyExist(String name){
            try {
                ugvs ugv=ugvRepository.CheckUgvAlreadyExist(name);
                return ugv;
            }catch (Exception ex){
                return null;
            }

    }
    public ugvs CheckUgvAlreadyExistById(int ugvid){
            try {
                ugvs ugv=ugvRepository.CheckUgvAlreadyExistById(ugvid);
                return ugv;
            }catch (Exception ex){
                return null;
            }

    }

    public ugvs CheckUgvAlreadyExistForUpdate(int ugvid,String name){
        try {
            ugvs ugv=ugvRepository.CheckUgvAlreadyExistForUpdate(ugvid,name);
            return ugv;
        }catch (Exception ex){
            return null;
        }

    }

    public int AddUGV(UGVBindingModel model){
        try {
            int Status = ugvRepository.AddUGV(model.ip,model.port,model.cameraip,model.name);
            return Status;
        }catch (Exception ex){
            return 0;
        }
    }
    public int UpdateUGV(UGVBindingModel model){
        try{
            int Status = ugvRepository.UpdateUGV(model.ip,model.port,model.cameraip,model.name,model.ugvid);
            return Status;
        }catch (Exception ex){
            return 0;
        }
    }

    public ugvs GetUGVById(int ugvid){
        try {
            ugvs ugv=ugvRepository.GetUGVById(ugvid);
            if(ugv != null){
                return ugv;
            }else{
                return null;
            }
        }catch (Exception ex){
            return null;
        }
    }








    //map services

    public List<Map> GetAllMaps(){
        try {
            List<Map> map = mapRepository.GetAllMaps();
            return map;
        }catch (Exception ex){
            return null;
        }
    }

    public Map CheckMapAlreadyExistForUpdate(int mapid,String name){
        try {
            Map map=mapRepository.CheckMapAlreadyExistForUpdate(mapid,name);
            return map;
        }catch (Exception ex){
            return null;
        }

    }

    public Map CheckMapAlreadyExist(String name){
        try {
            Map map=mapRepository.CheckMapAlreadyExist(name);
            return map;
        }catch (Exception ex){
            return null;
        }

    }

    public Map CheckMapAlreadyExistById(int mapid){
        try {
            Map map=mapRepository.CheckMapAlreadyExistById(mapid);
            return map;
        }catch (Exception ex){
            return null;
        }

    }

    public int AddMap(Map map){
        try {
            int Status = mapRepository.AddMap(map.getName(),map.getFilepath(),map.getDetails(),map.getResolution(),map.getOrigin()
                    ,map.getOrigingps(),map.getWidth(),map.getLength());
            return Status;
        }catch (Exception ex){
            return 0;
        }
    }

    public int AddMap(MapBindingModel map){
        try {
            int Status = mapRepository.AddMap(map.name,map.filepath,map.details);
            return Status;
        }catch (Exception ex){
            return 0;
        }
    }


    public int UpdateMap(Map map){
        try{
            int Status = mapRepository.UpdateMap(map.getId(),map.getName(),map.getFilepath(),map.getDetails(),map.getResolution(),map.getOrigin()
                    ,map.getOrigingps(),map.getWidth(),map.getLength());
            return Status;
        }catch (Exception ex){
            return 0;
        }
    }


//
//    public int UpdateMapInternally(Map map){
//        try{
//            int Status = mapRepository.UpdateMapInternally(map.getId(),map.getResolution(),map.getOrigin()
//                    ,map.getOrigingps(),map.getWidth(),map.getLength());
//            return Status;
//        }catch (Exception ex){
//            return 0;
//        }
//    }

    public Map GetMapById(int mapid){
        try {
            Map map=mapRepository.GetMapById(mapid);
            if(map != null){
                return map;
            }else{
                return null;
            }
        }catch (Exception ex){
            return null;
        }
    }


}
