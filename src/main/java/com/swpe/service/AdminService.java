package com.swpe.service;
import com.swpe.BindingModels.AddUserBindingModel;
import com.swpe.BindingModels.DeleteEntityBindingModel;
import com.swpe.BindingModels.MapBindingModel;
import com.swpe.BindingModels.UGVBindingModel;
import com.swpe.entity.Map;
import com.swpe.entity.ugvs;

import java.util.List;


public interface AdminService {

    public int AddUser(AddUserBindingModel bm);
    public int UpdateUser(AddUserBindingModel bm);
    public int DeleteEntity(int id,int type);
    public int ResetPassword(int userid);


    public List<ugvs> GetAllUGV();
    public ugvs CheckUgvAlreadyExistForUpdate(int ugvid,String name);
    public int AddUGV(UGVBindingModel model);
    public int UpdateUGV(UGVBindingModel model);
    public ugvs CheckUgvAlreadyExist(String name);
    public ugvs CheckUgvAlreadyExistById(int ugvid);
    public ugvs GetUGVById(int ugvid);


    public List<Map> GetAllMaps();
    public Map CheckMapAlreadyExistForUpdate(int mapid,String name);

    public int AddMap(MapBindingModel model);
    public int AddMap(Map map);
    public int UpdateMap(Map map);

    //public int UpdateMap(MapBindingModel model);
    //public int UpdateMapInternally(Map map);
    public Map CheckMapAlreadyExist(String name);
    public Map CheckMapAlreadyExistById(int mapid);

    public Map GetMapById(int mapid);


}
