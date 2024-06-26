package org.goafabric.calleeservice.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.util.Base64;

public class JWTVerifier {

    public static void main(String[] args) {
        try {
            String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJ5b3VyLWFwcGxpY2F0aW9uIiwic3ViIjoidXNlcm5hbWUxMjMiLCJwZXJtaXNzaW9ucyI6WyJyZWFkIiwid3JpdGUiLCJkZWxldGUiXX0.nREQQYU0CdCIPpfeQJlVfGlHS2eevYtOJc5m8MMBi7JWv6zAJHzP6SkVqEyo0260po0TaK_hyt-Ox1JUK1IL_VqTHuMo6Q1qz4-_JBnvqgsJQQv3gH3Jqhf07WXdoYbr8wr6Gfky0AxTnJ4VEjbIhlj1kb6sunLYGREgZ3L55vUEyBl4ivJJ7V5fHZVv3hBu1Dojmr4lVmd-ZfBUtZj5MLuVRBtH9eiA4wMXxcp6_WNuEUkH9t1RD-gds4gm7biu7ifnZc26FyR9zE4SCXd0niGDpAxHt_k4AGNfYPfQbtMaZR_-uLqZ13X1vSBuw0RGNlumNSmbYNmIQI1MZTjezA";
            var signedJWT = parseToken(token);
            logClaims(signedJWT);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static SignedJWT parseToken(String token) throws ParseException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, JOSEException {
        var signedJWT = SignedJWT.parse(token);
        var verifier = new RSASSAVerifier(readAndParsePublicKey());

        if (!signedJWT.verify(verifier)) {
            throw new IllegalStateException("JWT is invalid");
        }
        return signedJWT;
    }

    private static void logClaims(SignedJWT signedJWT) throws ParseException {
        var claims = signedJWT.getJWTClaimsSet();
        System.out.println("Username: " + claims.getSubject());
        System.out.println("Permissions: " + claims.getStringListClaim("permissions"));
    }

    private static RSAPublicKey readAndParsePublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        var publicKeyPEM  =
                new String(Files.readAllBytes(new ClassPathResource("keys/public-key.pem").getFile().toPath()))
                        .replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "").replaceAll("\\s", "");

        var decoded = Base64.getDecoder().decode(publicKeyPEM);
        var keySpec = new X509EncodedKeySpec(decoded);
        var keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }
}
