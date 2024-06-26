package org.goafabric.calleeservice.jwt;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class JWTVerifier {

    public static void main(String[] args) {
        try {
            // The JWT string to be verified
            String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJ5b3VyLWFwcGxpY2F0aW9uIiwic3ViIjoidXNlcm5hbWUxMjMiLCJleHAiOjE3MTk0MDYxODgsInBlcm1pc3Npb25zIjpbInJlYWQiLCJ3cml0ZSIsImRlbGV0ZSJdfQ.W7uqa591dqCIi-AYtlg8B9k7XRYTNd80RG0gr-rgYchJtL8Fgp3ydkvKPfDfRCU1FapC_So7UjIIwcVT4AfDHk7-Gl7y_p_2BoHrim88IzvfJl1Y-vAXm8KOAGs-9CgLQ2jbqBrxEiSEa2j5RjBACMGgWJpE5MvjHvTw5FtBEkHtnGwnWYcBYLYc1hW5fCGyxSFo4IcjqTKsXe8hvh3yOtajUlwNiMDYRaC0v1iOWSumXcJXV_Lbfm3oEy8QRWPjCzamN-WU80Thh1iKIKmlFSmNUdnW6PSv3gMn87eu-Wh0qT4JyWOuGF3RdvAnDmMRru3bjijuUrZayq9d2Uk2IQ";  // Replace with your JWT string

            // Your public key in PEM format
            String publicKeyPEM = "-----BEGIN PUBLIC KEY-----\n" +
                    "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtWzbzBW4OY7PqMnJ4ZNrWu/PkACIxJsFCALSPh2bSOVv5nVYe4PWlIJIowId673pphb8Uv6io1BHisWJfCqfmuMrRvq/t4Xt5Kb0V71n+KLMn52vl6EMv71cdDUzWbq3TFFiKbHqQ4r/QouJVJhBZJQOkr/grvo0pyUv5Srg7ikkdlEisbHuQJuNkNWTvRRDMG3sIudZK6ZegNfvUOz2tsiaclKOb3hIl+NP/XC9w93xvysX3FYl8/Gj4/FU1WAQKedCfJbsBRrA/oDgoavxM1JehgZK76r8/vjKYVnLdelW43uDL840q2yDxcAe8zVyKfgCIS9YHAEptHo8PZF1dQIDAQAB\n" +
                    "-----END PUBLIC KEY-----";  // Replace with your actual public key

            // Remove the first and last lines
            publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "").replaceAll("\\s", "");

            // Decode the base64 encoded string
            byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);

            // Generate RSA PublicKey from decoded bytes
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);

            // Parse the JWT
            SignedJWT signedJWT = SignedJWT.parse(token);

            // Create RSA verifier
            JWSVerifier verifier = new RSASSAVerifier(publicKey);

            // Verify the token
            if (signedJWT.verify(verifier)) {
                System.out.println("Token is valid");

                // Get the JWT claims
                JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

                System.out.println("Username: " + claims.getSubject());
                System.out.println("Permissions: " + claims.getStringListClaim("permissions"));
            } else {
                System.out.println("Token is invalid");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
