package org.goafabric.calleeservice.jwt;


import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

public class KeyGenerator {

    public static void main(String[] args) {
        try {
            // Generate RSA key pair
            KeyPair keyPair = generateRSAKeyPair();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

            // Serialize public key to be used by clients
            String privateKeyPEM = encodePrivateKeyToPEM(privateKey);
            System.out.println("private Key: \n" + privateKeyPEM);

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

    private static String encodePrivateKeyToPEM(RSAPrivateKey privateKey) {
        String base64PublicKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        return "-----BEGIN PRIVATE KEY-----\n" + base64PublicKey + "\n-----END PRIVATE KEY-----";
    }
}
