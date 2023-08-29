package com.swpe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ugvs")
public class ugvs implements Serializable {

    @javax.persistence.Id
    int id;
    String ip;
    String port;
    String cameraip;
    String name;
    String model;
    String manufacturer;
    Date starttime;
    Date warrenty;
    int isdeleted;

    public int getid(){return id;}

    public int getIsdeleted() {
        return isdeleted;
    }

    public String getCameraip() {
        return cameraip;
    }

    public String getIp() {
        return ip;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public String getName() {
        return name;
    }

    public String getPort() {
        return port;
    }

    public Date getStarttime() {
        return starttime;
    }

    public Date getWarrenty() {
        return warrenty;
    }

    // setters
    public void setid(int id) {
        id = id;
    }

    public void setCameraip(String cameraip) {
        this.cameraip = cameraip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setIsdeleted(int isdeleted) {
        this.isdeleted = isdeleted;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setStarttime(Timestamp starttime) {
        this.starttime = starttime;
    }

    public void setWarrenty(Timestamp warrenty) {
        this.warrenty = warrenty;
    }

}
