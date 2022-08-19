package org.xiaowu.behappy.screw.common.core.util;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.*;

import java.util.Date;

public class TokenUtils {

    private static final long TOKEN_EXPIRATION = 24 * 60 * 60 * 1000;

    private static final String TOKEN_SIGN_KEY = "60ea584a71754b7bb6128dc6c73f29d1";

    /**
     * 生成token
     *
     * @return
     */
    public static String genToken(Integer userId, String userName) {
        String token = Jwts.builder()
                .setSubject("YYGH-USER")
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
                .claim("userId", userId)
                .claim("userName", userName)
                .signWith(SignatureAlgorithm.HS512, TOKEN_SIGN_KEY)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        return token;
    }

    public static Integer getUserId(String token) {
        if (StrUtil.isEmpty(token)) {
            return null;
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(TOKEN_SIGN_KEY).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        Integer userId = (Integer) claims.get("userId");
        return userId;
    }

    public static String getUserName(String token) {
        if (StrUtil.isEmpty(token)) {
            return "";
        }
        Jws<Claims> claimsJws
                = Jwts.parser().setSigningKey(TOKEN_SIGN_KEY).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return (String) claims.get("userName");
    }

}
