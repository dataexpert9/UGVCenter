package com.swpe.service.impl;

import com.swpe.BindingModels.CreateLogBindingModel;
import com.swpe.dao.LogsRepository;
import com.swpe.entity.logs;
import com.swpe.entity.ugvs;
import com.swpe.service.LogService;
import com.swpe.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private LogsRepository logsRepository;


    public int CreateLog(CreateLogBindingModel bm){
        try {
            int Status = logsRepository.CreateLog(bm.actiontype,bm.subactiontype ,bm.description);
            if (Status == 1) {
                return 1;
            } else
                return 0;
        }catch(Exception ex){
            System.out.println("Exception occured in Create Logs LogServiceImpl"+ex);
            return 0;
        }
    }

    public List<logs> GetAllLogs(){
        try {
            List<logs> logs = logsRepository.GetAllLogs();
            return logs;
        }catch (Exception ex){
            System.out.println("Exception occured in Get All Logs "+ex);
            return null;
        }
    }

    public logs GetLogById(int id){
        try {
            logs log = logsRepository.GetLogById(id);
            return log;
        }catch (Exception ex){
            System.out.println("Exception occured in Get Log by id "+ex);
            return null;
        }
    }

}
