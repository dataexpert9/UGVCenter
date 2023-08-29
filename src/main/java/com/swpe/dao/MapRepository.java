package com.swpe.dao;

import com.swpe.entity.Map;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface MapRepository extends BaseRepository<Map,Long>, JpaSpecificationExecutor<Map> {

    @Query(nativeQuery = true,value = "select * from map where isdeleted=0")
    public List<Map> GetAllMaps();

    @Query(nativeQuery = true,value = "select * from map where name=?2 where id!=?1 and isdeleted=0")
    public Map CheckMapAlreadyExistForUpdate(int mapid,String name);

    @Query(nativeQuery = true,value = "select * from map where name=?1 and isdeleted=0")
    public Map CheckMapAlreadyExist(String name);

    @Query(nativeQuery = true,value = "select * from map where id=?1 and isdeleted=0")
    public Map CheckMapAlreadyExistById(int id);


    @Query(nativeQuery = true,value = "insert into map(name,filepath,details,resolution,origin,origingps,width,length,isdeleted) VALUES(?1,?2,?3,?4,?5,?6,?7,?8,0)")
    @Transactional
    @Modifying
    public int AddMap(String name,String filepath,String details,double resolution,String origin,String origingps,int width,int length);

    @Query(nativeQuery = true,value = "insert into map(name,filepath,details,isdeleted) VALUES(?1,?2,?3,0)")
    @Transactional
    @Modifying
    public int AddMap(String name,String filepath,String details);


    @Query(nativeQuery = true,value = "update map SET name=?2,filepath=?3,details=?4,resolution=?5,origin=?6,origingps=?7,width=?8,length=?9 where id=?1 and isdeleted=0")
    @Transactional
    @Modifying
    public int UpdateMap(int mapid,String name,String filepath,String details,double resolution,String origin,String origingps,int width,int length);

//    @Query(nativeQuery = true,value = "update map SET resolution=?2,origin=?3,origingps=?4,width=?5,length=?6 where id=?1 and isdeleted=0")
//    @Transactional
//    @Modifying
//    public int UpdateMapInternally(int mapid,double resolution,String origin,String origingps,int width,int length);


    @Query(nativeQuery = true, value = "select * from map where  id=?1 and isdeleted=0")
    public Map GetMapById(int mapid);
}
