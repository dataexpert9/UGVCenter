package com.swpe.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "logs")
public class logs implements Serializable {

    @javax.persistence.Id
    int id;
    int actiontype;
    int subactiontype;
    String description;
    Date created_date;
    Date updated_date;
    int isdeleted;


    public int getId() {
        return id;
    }

    public int getIsdeleted() {
        return isdeleted;
    }

    public Date getCreatedDate() {
        return created_date;
    }

    public Date getUpdatedDate() {
        return updated_date;
    }

    public int getActiontype() {
        return actiontype;
    }

    public String getDescription() {
        return description;
    }

    public int getSubactiontype() {
        return subactiontype;
    }

    public void setSubactiontype(int subactiontype) {
        this.subactiontype = subactiontype;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIsdeleted(int isdeleted) {
        this.isdeleted = isdeleted;
    }

    public void setActiontype(int actiontype) {
        this.actiontype = actiontype;
    }

    public void setCreatedDate(Date created_date) {
        this.created_date = created_date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUpdatedDate(Date updated_date) {
        this.updated_date = updated_date;
    }

}
