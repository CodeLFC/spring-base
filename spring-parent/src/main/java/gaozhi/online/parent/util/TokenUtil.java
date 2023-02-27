package gaozhi.online.parent.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author http://gaozhi.online
 * @version 1.0
 * @description: TODO JWT token 工具类
 * @date 2022/11/5 8:44
 */
public class TokenUtil {
    /**
     * ISSUER
     */
    private static final String ISSUER = "gaozhi.online";
    /**
     * AUDIENCE
     */
    private static final String AUDIENCE = "Client";
    /**
     * 秘钥
     */
    private static final String KEY = "marrymelmn";
    /**
     * 算法
     */
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(TokenUtil.KEY);
    /**
     * 头部
     */
    private static final Map<String, Object> HEADER_MAP = new HashMap<>() {
        {
            put("alg", "HS256");
            put("typ", "JWT");
        }
    };

    /**
     * 生成 Token 字符串
     *
     * @param claimMap   负载数据
     * @param expireTime 过期时间
     * @return Token 字符串
     */
    public static String generateToken(Map<String, String> claimMap, long expireTime) {
        //Token 建造器
        JWTCreator.Builder tokenBuilder = JWT.create();

        for (Map.Entry<String, String> entry : claimMap.entrySet()) {
            //Payload 部分，根据需求添加
            tokenBuilder.withClaim(entry.getKey(), entry.getValue());
        }

        //token 字符串: Header 部分 issuer audience 生效时间 过期时间 签名，算法加密
        return tokenBuilder.withHeader(TokenUtil.HEADER_MAP)
                .withIssuer(TokenUtil.ISSUER)
                .withAudience(TokenUtil.AUDIENCE)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(expireTime))
                .sign(TokenUtil.ALGORITHM);
    }

    /**
     * 校验失败会抛出异常
     */
    public static DecodedJWT verify(String token)  throws JWTVerificationException {
        return JWT.require(Algorithm.HMAC256(KEY)).build().verify(token);
    }
}

