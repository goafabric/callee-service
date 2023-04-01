package org.goafabric.calleeservice.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfiguration {

    //this is needed for the security context to live inside new threads
    @Bean
    public DelegatingSecurityContextAsyncTaskExecutor taskExecutor(ThreadPoolTaskExecutor asyncTaskExecutor) {
        return new DelegatingSecurityContextAsyncTaskExecutor(asyncTaskExecutor);
    }

    //@Bean are like factories, method names should be nouns that represent the type (asyncTaskExecutor)
    //can then be wired by variable name (see above), no need for Bean names or Qualifiers
    @Bean
    ThreadPoolTaskExecutor asyncTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(1);
        taskExecutor.setMaxPoolSize(10);
        //that's more than enough, rule of thump we need 0,5 CPU per Thread we spawn, so for 10 we already need 5
        //@see: https://wiki.postgresql.org/wiki/Number_Of_Database_Connections
        //number of active connections should be somewhere near ((core_count * 2) + effective_spindle_count)
        return taskExecutor;
    }
}
