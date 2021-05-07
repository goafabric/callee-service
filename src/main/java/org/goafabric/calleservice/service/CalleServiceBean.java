package org.goafabric.calleservice.service;

import lombok.experimental.Delegate;
import org.goafabric.calleservice.logic.CalleLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalleServiceBean implements CalleService {
    @Autowired
    @Delegate
    private CalleLogic calleLogic;
}
