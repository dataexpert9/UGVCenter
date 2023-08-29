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
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class User implements Serializable {

    @javax.persistence.Id
    int id;
    String username;
    String name;
    @JsonIgnore
    String password;
    int role;
    int isdeleted;
    String token;


    public int getId(){return id;}

    public String getUserName() {return username;}

    public String getPassword() {return password;}

    public String getFullName(){return name;}

    public int getRole() {return role;}

    public String getToken() {return token;}

    public int getIsDeleted() {return isdeleted;}


    // setters
    public void setId(int id) {
        id = id;
    }

    public void setFullName(String name) {this.name = name;}

    public void setUserName(String userName) {username = userName;}

    public void setPassword(String password) { password = password; }

    public void setRole(int role) {this.role = role;}

    public void setToken(String token) {this.token = token;}

    public void setIsDeleted(int isdeleted) {this.isdeleted = isdeleted;}
}
