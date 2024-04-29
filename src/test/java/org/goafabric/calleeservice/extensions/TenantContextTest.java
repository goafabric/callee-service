package org.goafabric.calleeservice.extensions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TenantContextTest {

    @Test
    void getTenantId() {
        TenantContext.setContext(null, null, null, null);
        assertThat(TenantContext.getTenantId()).isEqualTo("0");
    }

    @Test
    void getOrganizationId() {
        TenantContext.setContext(null, null, null, null);
        assertThat(TenantContext.getOrganizationId()).isEqualTo("0");
    }

    @Test
    void getUserName() {
        TenantContext.setContext(null, null, null, null);
        assertThat(TenantContext.getUserName()).isEqualTo("anonymous");
    }
    
    @Test
    void setTenantId() {
        TenantContext.setContext(null, null, null, null);
        TenantContext.setTenantId("42");
        assertThat(TenantContext.getTenantId()).isEqualTo("42");
    }

    @Test
    void getUserNameFromUserINfo() {
        var userInfo = "eyJwcmVmZXJyZWRfdXNlcm5hbWUiOiJqb2huIGRvZSIsImFsZyI6IkhTMjU2In0";
        TenantContext.setContext("42", "44", "user", userInfo);
        assertThat(TenantContext.getUserName()).isEqualTo("john doe");
    }

    @Test
    void getAll() {
        TenantContext.setContext("42", "44", "user", null);
        assertThat(TenantContext.getTenantId()).isEqualTo("42");
        assertThat(TenantContext.getOrganizationId()).isEqualTo("44");
        assertThat(TenantContext.getUserName()).isEqualTo("user");
        assertThat(TenantContext.getAdapterHeaderMap()).isNotNull().isNotEmpty();
    }


}