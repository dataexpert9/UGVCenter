package com.swpe.dao;

import com.swpe.entity.User;
import org.hibernate.annotations.SQLInsert;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;


public interface UserRepository extends BaseRepository<User,Long>, JpaSpecificationExecutor<User> {



    @Query(nativeQuery = true, value = "select * from users where  username=?1 AND password=?2 and isdeleted=0")
    public User Login(String Username, String Password);

    @Query(nativeQuery = true,value = "select * from users where isdeleted=0")
    public List<User> GetAllUsers();


    @Query(nativeQuery = true, value = "select * from users where  username=?1 and isdeleted=0")
    public User UsernameExists(String Username);


    @Query(nativeQuery = true, value = "select * from users where id!=?1 AND username=?2 and isdeleted=0")
    public User CheckUsernameForEditUser(int id,String Username);

    @Query(nativeQuery = true, value = "select * from users where  id=?1 and isdeleted=0")
    public User GetUserById(int id);

    @Query(nativeQuery = true,value = "update users set loginattempts=?1,lastupdate=NOW() where id=?2 and isdeleted=0")
    @Transactional
    @Modifying
    public int UpdateLoginAttempts(int loginattempts,int UserId);

    @Query(nativeQuery = true,value = "update users set status=0,lastupdate=NOW()  where id=?1 and isdeleted=0")
    @Transactional
    @Modifying
    public int DisableUserAccount(int UserId);


    @Query(nativeQuery = true,value = "update users set status=1,lastupdate=NOW(),loginattempts=0  where id=?1 and isdeleted=0")
    @Transactional
    @Modifying
    public int ResetUserAccountAfterBan(int UserId);

    @Query(nativeQuery = true,value = "select * from users where Id=?1 AND username=?3 AND role=?2 and isdeleted=0")
    public User ValidateLogin(int userid, int role, String username);

    @Query(nativeQuery = true,value = "update users set password=?3 where id=?1 AND password=?2 and isdeleted=0")
    @Transactional
    @Modifying
    public int ChangePassword(int id,String oldpassword,String newpassword);
}
