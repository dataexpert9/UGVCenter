package com.swpe.dao;

import com.swpe.entity.Map;
import com.swpe.entity.User;
import com.swpe.entity.logs;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface LogsRepository extends BaseRepository<logs,Long>, JpaSpecificationExecutor<logs> {

    @Query(nativeQuery = true,value = "insert into logs(actiontype,subactiontype,description,created_date,updated_date,isdeleted) VALUES(?1,?2,?3,NOW(),NOW(),0)")
    @Transactional
    @Modifying
    public int CreateLog(int actiontype,int subactiontype, String description);

    @Query(nativeQuery = true,value = "select * from logs where isdeleted=0")
    public List<logs> GetAllLogs();

    @Query(nativeQuery = true, value = "select * from logs where  id=?1 and isdeleted=0")
    public logs GetLogById(int id);


}
