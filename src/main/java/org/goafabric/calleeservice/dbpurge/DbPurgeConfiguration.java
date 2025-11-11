//package org.goafabric.calleeservice.dbpurge;
//
//import com.zaxxer.hikari.HikariDataSource;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Consumer;
//import java.util.stream.Stream;
//
////implementation("org.springframework.boot:spring-boot-starter-jdbc"); implementation("com.h2database:h2")
//@Configuration
//public class DbPurgeConfiguration {
//    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());
//
//    @Bean
//    public Map<String, JdbcTemplate> jdbcTemplates(MultiDataSourceProperties multiProps, @Value("${purge.poolsize}") Integer poolSize) {
//        Map<String, JdbcTemplate> templates = new HashMap<>();
//
//        multiProps.getDatasource().forEach((name, props) -> {
//            HikariDataSource ds = new HikariDataSource();
//            ds.setJdbcUrl(props.url());
//            ds.setUsername(props.username());
//            ds.setPassword(props.password());
//            ds.setPoolName("Hikari-" + name);
//            ds.setMaximumPoolSize(poolSize);
//
//            templates.put(name, new JdbcTemplate(ds));
//        });
//
//        return templates;
//    }
//    @Bean
//    Consumer<String> dbRunner(Map<String, JdbcTemplate> jdbcTemplates,
//                              MultiDataSourceProperties multiProps) {
//        return tenantId -> {
//            jdbcTemplates.forEach((name, jdbcTemplate) -> {
//                String sqlScript = multiProps.getDatasource().get(name).sqlscript();
//                Stream.of(sqlScript.split(";"))
//                        .map(sql -> sql.replaceAll("%TENANT_ID%", tenantId))
//                        .forEach(sql -> {
//                            log.info("Executing on [{}]: {}", name, sql);
//                            jdbcTemplate.execute(sql);
//                        });
//            });
//        };
//    }
//
//    @Configuration
//    @ConfigurationProperties(prefix = "purge")
//    class MultiDataSourceProperties {
//        private Map<String, DataSourceProperties> datasource;
//        public Map<String, DataSourceProperties> getDatasource() { return datasource; }
//        public void setDatasource(Map<String, DataSourceProperties> datasource) { this.datasource = datasource;}
//
//        public record DataSourceProperties(String url, String username, String password, String sqlscript) {}
//    }
//
//
//}
