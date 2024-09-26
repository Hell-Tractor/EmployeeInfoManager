package com.employeeinfomanager.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.employeeinfomanager.aop.AuditLevel;
import com.employeeinfomanager.aop.UserToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class JwtHelper {
    private final Logger logger = LoggerFactory.getLogger(JwtHelper.class);

    static final String SECRET = "LhARRD2d0HpALfm7";
    static final String ISSUER = "EmployeeInfoManager";
    public static final String LOGIN_TOKEN_KEY = "authorization";

    public String createToken(Long userId, String username, AuditLevel level, Long departId, int expireAfterSeconds) {
        logger.debug("Creating token...");
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            Map<String, Object> map = new HashMap<>();
            Date currentDate = new Date();
            Date expireDate = addSeconds(currentDate, expireAfterSeconds);
            map.put("alg", "HS256");
            map.put("typ", "JWT");
            String tokenId = Utils.genSeqNum(1);
            String message = "createToken:" +
                    " userId = " + userId +
                    " username = " + username +
                    " tokenId = " + tokenId;
            logger.debug(message);
            return JWT.create()
                    .withHeader(map)
                    .withClaim("userId", userId)
                    .withClaim("tokenId", tokenId)
                    .withClaim("username", username)
                    .withClaim("userLevel", level.ordinal())
                    .withClaim("departId", departId)
                    .withIssuer(ISSUER)
                    .withIssuedAt(currentDate)
                    .withExpiresAt(expireDate)
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            logger.error(exception.toString());
        }
        return null;
    }

    public UserToken verifyTokenAndGetClaims(String token) {
        if (token == null || token.isEmpty())
            return null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
            DecodedJWT jwt = verifier.verify(token);
            Map<String, Claim> claims = jwt.getClaims();
            return new UserToken(
                    claims.get("userId").asLong(),
                    claims.get("username").asString(),
                    jwt.getExpiresAt(),
                    AuditLevel.values()[claims.get("userLevel").asInt()],
                    claims.get("departId").asLong()
            );
        } catch (JWTVerificationException exception) {
            logger.error(exception.toString());
        }
        return null;
    }

    private Date addSeconds(Date start, int seconds) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(start);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }
}
