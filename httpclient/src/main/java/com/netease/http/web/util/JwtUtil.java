package com.netease.http.web.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author system
 */
public class JwtUtil {
    public final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    private String secret;

    public JwtUtil(String secret) {
        this.secret = secret;
    }

    public Map<String, Claim> decryptToken(String token) throws JWTVerificationException {
        //服务器时间不同步处理方式--允许未来5分钟内的产生的Token
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret.getBytes())).acceptIssuedAt(300).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getClaims();
    }

}