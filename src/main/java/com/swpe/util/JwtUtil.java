package com.swpe.util;


import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

    // @Value("${spring.profiles.active}")
    // private String profiles;

    /**
     * Generate key by Stringkey
     *
     * @return
     */
    private SecretKey generalKey() {
        String stringKey = "1286df7fc3a34e26a61c034d5ec8245d";
        byte[] encodedKey = Base64.decodeBase64(stringKey);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * Generate jwt
     *
     * @param id
     * @param subject
     * @param ttlMillis
     * @return
     * @throws Exception
     */
    public String createJWT(String id, String subject, long ttlMillis) throws Exception {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        SecretKey key = generalKey();
        JwtBuilder builder = Jwts.builder().setId(id).setIssuedAt(now).setSubject(subject).signWith(signatureAlgorithm,
                key);
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    /**
     * Decryption jwt
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public Claims parseJWT(String jwt) throws Exception {
        SecretKey key = generalKey();
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody();
        return claims;
    }

    /**
     * Get JSON from token
     *
     * @param token
     * @return
     */
    public JSONObject getJson(String token) {
        if (null == token) {
            return null;
        }
        Claims claims;
        try {
            claims = parseJWT(token);
            String userString = claims.getSubject();
            JSONObject userJSON = JSON.parseObject(userString);
            return userJSON;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Generate Subject
     *
     * @param
     * @return
     */
    public String generalSubject(int id,String username, String name, int role) {
        JSONObject jo = new JSONObject();
        jo.put("uniqueid",id);
        jo.put("username", username);
        jo.put("name", name);
        jo.put("role", role);
        return jo.toJSONString();
    }

    /**
     * Check token legality
     *
     * @param token
     * @return
     */
    public boolean isLegal(String token) {
        try {
            parseJWT(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
