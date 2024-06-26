package org.goafabric.calleeservice.jwt;


import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.Base64;

public class JWTIssuer {

    public static void main(String[] args) {
        try {
            // Generate RSA key pair
            KeyPair keyPair = generateRSAKeyPair();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

            // Create a new RSA signer
            JWSSigner signer = new RSASSASigner(privateKey);

            // Prepare JWT with claims set
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject("username123")
                    .claim("permissions", Arrays.asList("read", "write", "delete"))
                    .issuer("your-application")
                    //.expirationTime(new Date(new Date().getTime() + 6000 * 1000)) // 100 minute expiration
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader.Builder(JWSAlgorithm.RS256).type(JOSEObjectType.JWT).build(),
                    claimsSet);

            // Apply the RSA signature
            signedJWT.sign(signer);

            // Serialize the JWT to a compact form
            String token = signedJWT.serialize();
            System.out.println("Generated Token: " + token);

            // Serialize public key to be used by clients
            String publicKeyPEM = encodePublicKeyToPEM(publicKey);
            System.out.println("Public Key: \n" + publicKeyPEM);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to generate RSA key pair
    private static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    // Method to encode RSA public key to PEM format
    private static String encodePublicKeyToPEM(RSAPublicKey publicKey) {
        String base64PublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        return "-----BEGIN PUBLIC KEY-----\n" + base64PublicKey + "\n-----END PUBLIC KEY-----";
    }
}
