package org.goafabric.calleeservice.crossfunctional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.core.VaultKeyValueOperationsSupport;
import org.springframework.vault.core.VaultTemplate;

@Configuration
public class VaultConfiguration {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private VaultTemplate vaultTemplate;

    @Bean
    public CommandLineRunner vault() {
        return args -> {
            //if (!((args.length > 0) && ("-vault".equals(args[0])))) { return; }
            createCredentials();
        };
    }

    private void createCredentials() {
        record MySecret(String myKey, String myValue) {}

        var mySecret = new MySecret("secretkey", "secret-key-12345");

        //allocate
        var vaultKeyValueOperations = vaultTemplate.opsForKeyValue("secret",
                VaultKeyValueOperationsSupport.KeyValueBackend.KV_2);

        //write
        vaultKeyValueOperations.put(mySecret.myKey, mySecret);

        //read
        var response = vaultKeyValueOperations.get(mySecret.myKey, MySecret.class);

        assert response != null && response.getData() != null;
        log.info(response.getData().myKey);
        log.info(response.getData().myValue);
    }
}


