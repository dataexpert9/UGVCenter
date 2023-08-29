package com.swpe.dao;

import com.swpe.entity.User;
import com.swpe.entity.ugvs;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface UGVRepository extends BaseRepository<ugvs,Long>, JpaSpecificationExecutor<ugvs> {

    @Query(nativeQuery = true,value = "select * from ugvs where isdeleted=0")
    public List<ugvs> GetAllUGV();

    @Query(nativeQuery = true,value = "select * from ugvs where name=?2 where id!=?1 and isdeleted=0")
    public ugvs CheckUgvAlreadyExistForUpdate(int ugvid,String name);

    @Query(nativeQuery = true,value = "select * from ugvs where name=?1 and isdeleted=0")
    public ugvs CheckUgvAlreadyExist(String name);

    @Query(nativeQuery = true,value = "select * from ugvs where id=?1 and isdeleted=0")
    public ugvs CheckUgvAlreadyExistById(int ugvid);

    @Query(nativeQuery = true,value = "insert into ugvs(ip,port,cameraip,name,isdeleted) VALUES(?1,?2,?3,?4,0)")
    @Transactional
    @Modifying
    public int AddUGV(String ip,String port,String cameraip,String name);

    @Query(nativeQuery = true,value = "update ugvs SET ip=?1,port=?2,cameraip=?3,name=?4 where id=?5 and isdeleted=0")
    @Transactional
    @Modifying
    public int UpdateUGV(String ip,String port,String cameraip,String name,int ugvid);


    @Query(nativeQuery = true, value = "select * from ugvs where  id=?1 and isdeleted=0")
    public ugvs GetUGVById(int ugvid);
}
