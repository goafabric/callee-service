package org.goafabric.calleeservice;

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

    record Credentials(String userName, String password) {}

    @Bean
    public CommandLineRunner vault() {
        return args -> {
            //if (!((args.length > 0) && ("-create-creds".equals(args[0])))) { return; }
            createCredentials();
        };
    }

    private void createCredentials() {
        var credentials = new Credentials("homer simpson", "super-secret");
        var vaultKeyValueOperations = vaultTemplate.opsForKeyValue("secret",
                VaultKeyValueOperationsSupport.KeyValueBackend.KV_2);

        //write
        vaultKeyValueOperations.put(credentials.userName, credentials);

        //read
        var response = vaultKeyValueOperations.get(credentials.userName, Credentials.class);

        assert response != null;
        log.info(response.getData().userName);
        log.info(response.getData().password());
    }

    //implementation("org.springframework.cloud:spring-cloud-starter-vault-config:4.0.0")
    //docker run --name vault --rm --cap-add=IPC_LOCK  -p8200:8200 -e 'VAULT_DEV_ROOT_TOKEN_ID=myroot' vault:1.13.1
}


