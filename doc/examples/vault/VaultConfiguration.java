package org.goafabric.calleeservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.core.VaultKeyValueOperationsSupport;
import org.springframework.vault.core.VaultTemplate;

import java.util.Objects;

@Configuration
public class VaultConfiguration {
    public record MySecret(String myValue) {}

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public VaultConfiguration() {
        createApplicationYamlReplacement();
    }

    private static void createApplicationYamlReplacement() {
        System.setProperty("spring.cloud.vault.uri", "http://localhost:8200");
        System.setProperty("spring.cloud.vault.authentication", "TOKEN");
        System.setProperty("spring.cloud.vault.token", "myroot");
    }

    @Bean
    public CommandLineRunner vault(MySecret mySecret) {
        return args -> {
            //if (!((args.length > 0) && ("-vault".equals(args[0])))) { return; }
            log.info(mySecret.myValue());
        };
    }

    //create a Bean so that we can access the value clean from everywhere spring
    @Bean MySecret mySecret(VaultTemplate vaultTemplate) {
        //allocate
        var vaultKeyValueOperations = vaultTemplate.opsForKeyValue("secret",
                VaultKeyValueOperationsSupport.KeyValueBackend.KV_2);

        //write
        vaultKeyValueOperations.put("secretkey", new MySecret("secret-key-12345"));

        //read
        return Objects.requireNonNull(vaultKeyValueOperations.get("secretkey", MySecret.class)).getData();
    }
}


