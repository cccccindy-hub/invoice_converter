package com.nnroad.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class ThirdAppTokenUtils {
    private static final long EXPIRATION_TIME = 1000*60*10; // 10分钟有效期

    // 生成token
    public static String generateToken(String appId,String appKey, String secretKey) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        String token = Jwts.builder()
                .claim("appId", appId)
                .claim("appKey", appKey)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return token;
    }

    // 解密token并获取appKey和SecretKey
    public static TokenInfo decodeToken(String token, String secretKey) {
        Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token);

        Claims claims = claimsJws.getBody();
        String appKey = claims.get("appKey", String.class);
        String appId = claims.get("appId", String.class);
        return new TokenInfo(appKey,appId);
    }

    // 内部类用于封装appKey和SecretKey
    public static class TokenInfo {
        private String appKey;
        private String appId;

        public TokenInfo(String appKey, String appId) {
            this.appKey = appKey;
            this.appId = appId;
        }

        public String getAppKey() {
            return appKey;
        }

        public String getAppId() {
            return appId;
        }
    }
}
