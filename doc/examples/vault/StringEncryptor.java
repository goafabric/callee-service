package org.goafabric.personservice.persistence;


import jakarta.persistence.AttributeConverter;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

// Adds Encryption to JPA Columns via Annotation (@Convert(converter = StringEncryptor.class))
// Works for all CRUD operations, as well as simple search, as long as a non RANDOM Salt is used
// Drawback vs Infrastructure Volume Encryption: Potentially Insecure, Error Prone and Inconsistent as it is handled individually for each App during Devlopment Time
// Source: https://sultanov.dev/blog/database-column-level-encryption-with-spring-data-jpa/
public class StringEncryptor implements AttributeConverter<String, String> {

    private final String secretKey;

    private final Key key;
    private final Cipher cipher;

    public StringEncryptor() throws Exception {
        secretKey = new String(Base64.getDecoder().decode("c2VjcmV0LWtleS0xMjM0NQ==")); //should be injected by @Value from a secure environment
        key = new SecretKeySpec(secretKey.getBytes(), "AES");
        cipher = Cipher.getInstance("AES");
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes()));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}