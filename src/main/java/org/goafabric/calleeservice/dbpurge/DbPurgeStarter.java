//package org.goafabric.calleeservice.dbpurge;
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.function.Consumer;
//
//@RestController
//public class DbPurgeStarter {
//    private Consumer<String> dbRunner;
//    public DbPurgeStarter(Consumer<String> dbRunner) {
//        this.dbRunner = dbRunner;
//        purge("5");
//    }
//
//    @GetMapping("/purge")
//    public void purge(@RequestParam("tenantid") String tenantId) {
//        dbRunner.accept(tenantId);
//    }
//}
