package org.goafabric.calleeservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.core.VaultKeyValueOperationsSupport;
import org.springframework.vault.core.VaultTemplate;

@Configuration
public class VaultConfiguration {
    record Credentials(String userName, String password) {}

    @Bean
    public CommandLineRunner vault(VaultTemplate vaultTemplate) {
        return args -> {
            //if ((args.length > 0) && ("-create-creds".equals(args[0]))) {
                var credentials = new Credentials("homer simpson", "super-secret");
                var vaultKeyValueOperations = vaultTemplate.opsForKeyValue("secret",
                        VaultKeyValueOperationsSupport.KeyValueBackend.KV_2);

                //write
                vaultKeyValueOperations.put(credentials.userName, credentials);

                //read
                var response = vaultKeyValueOperations.get(credentials.userName, Credentials.class);

                assert response != null;
                System.out.println(response.getData().userName);
                System.out.println(response.getData().password());
            //}
        };
    }

//implementation("org.springframework.cloud:spring-cloud-starter-vault-config:4.0.0")
}


