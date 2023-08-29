package com.swpe.BindingModels;

import javafx.scene.control.TextFormatter;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class LoginBindingModel {

    @NotNull(message = "username cannot be null")
    @NotEmpty(message = "username cannot be empty")
    public String username;

    public String password;

    public LoginBindingModel(String username, String password){
        this.username=username;
        this.password=password;
    }
}

