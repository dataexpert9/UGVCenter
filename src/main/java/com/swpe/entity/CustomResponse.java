package com.swpe.entity;

public class CustomResponse {

    public String message;
    public int statuscode;
    public Object result;

    public CustomResponse(){

    }
    public CustomResponse(String Message, int StatusCode, Object Result){
        this.message=Message;
        this.statuscode=StatusCode;
        this.result=Result;
    }

    public void setMessage(String message) {
        message = message;
    }

    public void setResult(Object result) {
        result = result;
    }

    public void setStatusCode(int statusCode) {
        statuscode = statusCode;
    }
}
