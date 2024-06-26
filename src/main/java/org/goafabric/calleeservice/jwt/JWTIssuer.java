package org.goafabric.calleeservice.jwt;


import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class JWTIssuer {

    public static void main(String[] args) {
        try {
            // Prepare JWT with claims set
            var claimsSet = new JWTClaimsSet.Builder()
                    .subject("username123")
                    .claim("permissions", Arrays.asList("read", "write", "delete"))
                    .issuer("your-application")
                    //.expirationTime(new Date(new Date().getTime() + 6000 * 1000)) // 100 minute expiration
                    .build();

            var token = createToken(claimsSet);
            System.out.println("Generated Token:\n" + token);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String createToken(JWTClaimsSet claimsSet) throws Exception {
        var signer = new RSASSASigner(readAndParsePrivateKey());
        var signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256).type(JOSEObjectType.JWT).build(),
                claimsSet);

        signedJWT.sign(signer);
        return signedJWT.serialize();
    }

    private static RSAPrivateKey readAndParsePrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        var publicKey  = new String(Files.readAllBytes(new ClassPathResource("keys/private-key.pem").getFile().toPath()))
                .replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "").replaceAll("\\s", "");

        byte[] encoded = Base64.getDecoder().decode(publicKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

}
