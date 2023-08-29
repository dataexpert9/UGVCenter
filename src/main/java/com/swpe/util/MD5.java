package com.swpe.util;

import java.security.MessageDigest;
import java.math.BigInteger;


public class MD5 {
    public static String getMD5(String str) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            result = new BigInteger(1, md.digest()).toString(16);
            while (32 != result.length()) {
                result = "0" + result;
            }
            return result;
        } catch (Exception e) {
            System.out.println("MD5 error");
        }
        return str;
    }
}
