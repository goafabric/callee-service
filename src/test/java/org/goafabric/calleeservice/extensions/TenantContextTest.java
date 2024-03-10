package org.goafabric.calleeservice.extensions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TenantContextTest {

    @Test
    void getTenantId() {
        TenantContext.setContext(new TenantContext.TenantContextRecord(null, null, null));
        assertThat(TenantContext.getTenantId()).isEqualTo("0");
    }

    @Test
    void getOrganizationId() {
        TenantContext.setContext(new TenantContext.TenantContextRecord(null, null, null));
        assertThat(TenantContext.getOrganizationId()).isEqualTo("1");
    }

    @Test
    void getUserName() {
        TenantContext.setContext(new TenantContext.TenantContextRecord(null, null, null));
        assertThat(TenantContext.getUserName()).isEqualTo("anonymous");
    }
    
    @Test
    void setTenantId() {
        TenantContext.setContext(new TenantContext.TenantContextRecord(null, null, null));
        TenantContext.setTenantId("42");
        assertThat(TenantContext.getTenantId()).isEqualTo("42");
    }

    @Test
    void getAll() {
        TenantContext.setContext(new TenantContext.TenantContextRecord("42", "44", "user"));
        assertThat(TenantContext.getTenantId()).isEqualTo("42");
        assertThat(TenantContext.getOrganizationId()).isEqualTo("44");
        assertThat(TenantContext.getUserName()).isEqualTo("user");
        assertThat(TenantContext.getAdapterHeaderMap()).isNotNull().isNotEmpty();
    }

}