package org.xiaowu.behappy.screw.util;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.*;
import org.xiaowu.behappy.screw.constant.ResStatus;
import org.xiaowu.behappy.screw.exception.ServiceException;

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
        return Jwts.builder()
                .setSubject("BEHAPPY")
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
                .claim("userId", userId)
                .claim("userName", userName)
                .signWith(SignatureAlgorithm.HS512, TOKEN_SIGN_KEY)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
    }

    public static Integer getUserId(String token) {
        try {
            if (StrUtil.isEmpty(token)) {
                return null;
            }
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(TOKEN_SIGN_KEY).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            return (Integer) claims.get("userId");
        } catch (Exception e) {
            throw new ServiceException(ResStatus.CODE_401,e.getMessage());
        }
    }

    public static String getUserName(String token) {
        try {
            if (StrUtil.isEmpty(token)) {
                return "";
            }
            Jws<Claims> claimsJws
                    = Jwts.parser().setSigningKey(TOKEN_SIGN_KEY).parseClaimsJws(token);
            Claims claims = claimsJws.getBody();
            return (String) claims.get("userName");
        } catch (Exception e) {
            throw new ServiceException(ResStatus.CODE_401,e.getMessage());
        }
    }

}
