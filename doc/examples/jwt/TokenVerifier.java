package org.goafabric.calleeservice.jwt;

import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
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
import java.util.stream.Collectors;

public class TokenVerifier {

    public static void main(String[] args) {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJzZWNyZXQtYXBwbGljYXRpb24iLCJzdWIiOiJqb2huQGRvZS5vcmciLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJqb2huQGRvZS5vcmciLCJwZXJtaXNzaW9ucyI6WyJyZWFkIiwid3JpdGUiLCJkZWxldGUiXSwidGVuYW50IjoiNSJ9.ltsAQWMvusBwGS8Y0Gg2wE6cj74L21iC6fDy81yElxypMNmPS1nKknEWpMI8jD5t5-epYc-hnMefmFODmgvjilENgcBbu2zf6n_1rsuczTkTzTjqY7Xw5g0zbo-JJz1zgrmdnUATBvumTZuy5fNMMTb2LshRmqNLOKCzF7H-cF7sv2boSqitfdnAwrrfOKBGhVdlEuD9OE8iarJqbACBo9-ccsW1Cr77J7eoOLzXf7DwGXWNqCyXjbs1n5lZlPKo74ozMF7WCmYaoA92to9SaiDTvM39EIRBjM6oxkP9kFTc2QXaL0w-72kYQqtAilEQczl7DTMuLhtYZK5xlPC5Rw";
        var claims = parseToken(token);

        logClaims(claims);
    }

    private static JWTClaimsSet parseToken(String token) {
        try {
            var signedJWT = SignedJWT.parse(token);
            if (!signedJWT.verify(new RSASSAVerifier(getPublicKey()))) {
                throw new IllegalStateException("JWT is invalid");
            }
            return signedJWT.getJWTClaimsSet();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static RSAPublicKey getPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        var publicKey = Files.readAllLines(new ClassPathResource("keys/public_key.pem").getFile().toPath())
                .stream().filter(line -> !line.contains("-----")).collect(Collectors.joining());
        var keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
        var keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    private static void logClaims(JWTClaimsSet claims) {
        try {
            System.out.println("Preferred Username: " + claims.getStringClaim("preferred_username"));
            System.out.println("Tenant: " + claims.getStringClaim("tenant"));
            System.out.println("Permissions: " + claims.getStringListClaim("permissions"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
