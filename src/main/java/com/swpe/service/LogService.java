package com.swpe.service;


import com.swpe.BindingModels.CreateLogBindingModel;
import com.swpe.entity.logs;

import java.util.List;

public interface LogService {

    public int CreateLog(CreateLogBindingModel bm);
    public List<logs> GetAllLogs();
    public logs GetLogById(int id);

}
