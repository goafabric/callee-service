package org.goafabric.calleeservice.s3;

import java.util.List;

public interface ObjectStorageLogic {
    ObjectEntry getById(String key);
    void save(ObjectEntry objectEntry);
    List<ObjectEntry> search(String search);
}

