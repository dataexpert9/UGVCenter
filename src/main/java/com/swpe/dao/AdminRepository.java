package com.swpe.dao;


import com.swpe.entity.User;
import com.swpe.entity.ugvs;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface AdminRepository extends BaseRepository<User,Long>, JpaSpecificationExecutor<User> {

    @Query(nativeQuery = true,value = "insert into users(username,role,name,password,isdeleted) VALUES(?1,?2,?3,?4,?5)")
    @Transactional
    @Modifying
    public int AddUser(String username,int role,String name,String password,int isdeleted);

    @Query(nativeQuery = true,value = "update users SET username=?2,role=?3,name=?4 where id=?1 and isdeleted=0")
    @Transactional
    @Modifying
    public int UpdateUser(int id,String username,int role,String name);


    @Query(nativeQuery = true,value = "update users SET isdeleted=1 where id=?1 and isdeleted=0")
    @Transactional
    @Modifying
    public int DeleteUser(int id);

    @Query(nativeQuery = true,value = "update ugvs SET isdeleted=1 where id=?1 and isdeleted=0")
    @Transactional
    @Modifying
    public int DeleteUGV(int id);

    @Query(nativeQuery = true,value = "update map SET isdeleted=1 where id=?1")
    @Transactional
    @Modifying
    public int DeleteMap(int id);

    @Query(nativeQuery = true,value = "update users SET password=?2 where id=?1 and isdeleted=0")
    @Transactional
    @Modifying
    public int ResetPassword(int userid,String password);




}
