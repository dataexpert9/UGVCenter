package com.swpe;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.util.Properties;

@EnableScheduling
@SpringBootApplication
public class SituationApplication {
    public static void main(String[] args) {
        SpringApplication.run(SituationApplication.class, args);


    }


}
