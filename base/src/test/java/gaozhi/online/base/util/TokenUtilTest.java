package gaozhi.online.base.util;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;



class TokenUtilTest {
    public static void main(String[] args) {
        Map<String, String> payload = new HashMap<>();
        payload.put("id", "10001");
        String token = TokenUtil.generateToken(payload, System.currentTimeMillis()+1000);
        DecodedJWT verify = TokenUtil.verify(token);
        System.out.println(verify.getClaim("id").asString());
    }
}