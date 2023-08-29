package com.swpe.BindingModels;

    public class ChangePasswordBindingModel {
        public String oldpassword;
        public String newpassword;

        public ChangePasswordBindingModel(String oldpassword, String newpassword) {
            this.oldpassword = oldpassword;
            this.newpassword = newpassword;
        }
    }
