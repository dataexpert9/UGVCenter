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
@Table(name = "Map")
public class Map implements Serializable {

    @javax.persistence.Id
    int id;
    String name;
    String filepath;
    String details;
    int isdeleted;

    public double resolution;
    public String origin;
    public String origingps;
    public int width;
    public int length;

    public double getResolution() {
        return resolution;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public String getOrigin() {
        return origin;
    }

    public String getOrigingps() {
        return origingps;
    }

    public int getIsdeleted() {
        return isdeleted;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getDetails() {
        return details;
    }

    public String getFilepath() {
        return filepath;
    }

    // setters

    public void setIsdeleted(int isdeleted) {
        this.isdeleted = isdeleted;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setOrigingps(String origingps) {
        this.origingps = origingps;
    }

    public void setResolution(double resolution) {
        this.resolution = resolution;
    }

    public void setWidth(int width) {
        this.width = width;
    }

}

