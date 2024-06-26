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
import java.util.stream.Collectors;

public class TokenIssuer {
    //public and private key creation
    //openssl genpkey -algorithm RSA -out private_key.pem -pkeyopt rsa_keygen_bits:2048 && openssl rsa -pubout -in private_key.pem -out public_key.pem

    public static void main(String[] args) {
        var claimsSet = new JWTClaimsSet.Builder()
                .subject("john@doe.org")
                .claim("preferred_username", "john@doe.org")
                .claim("tenant", "5")
                .claim("permissions", Arrays.asList("read", "write", "delete"))
                .issuer("secret-application")
                //.expirationTime(new Date(new Date().getTime() + 6000 * 1000)) // 100 minute expiration
                .build();

        var token = createToken(claimsSet);
        System.out.println("Generated Token:\n" + token);
    }

    private static String createToken(JWTClaimsSet claimsSet)  {
        try {
            var signedJWT = new SignedJWT(
                    new JWSHeader.Builder(JWSAlgorithm.RS256).type(JOSEObjectType.JWT).build(), claimsSet);
            signedJWT.sign(new RSASSASigner(getPrivateKey()));
            return signedJWT.serialize();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static RSAPrivateKey getPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        var privateKey = Files.readAllLines(new ClassPathResource("keys/private_key.pem").getFile().toPath())
                .stream().filter(line -> !line.contains("-----")).collect(Collectors.joining());
        var keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }
}
