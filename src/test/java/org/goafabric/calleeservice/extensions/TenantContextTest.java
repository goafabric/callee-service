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
        var userInfo = "eyJwcmVmZXJyZWRfdXNlcm5hbWUiOiJqb2huIGRvZSIsImFsZyI6IkhTMjU2In0"; //eyJnZW5kZXIiOiJVTktOT1dOIiwiYmlydGhkYXRlIjoiMTk4MC0wMS0wMSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJ1cm46Z29hZmFicmljOmNsYWltczppbnN0aXR1dGlvbiI6W3siaWQiOiI4MmZhY2RkZS0xOTVjLTQ3OGUtODczYS0yM2MzNDBmZmYyZWQiLCJyb2xlcyI6WyJNRU1CRVIiLCJBRE1JTiJdLCJuYW1lIjoiVGVzdCJ9XSwibmFtZSI6IkpvbmggRG9lIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiam9obi5kb2VAdW5rbm93bi5vcmciLCJlbWFpbCI6ImpvaG4uZG9lQHVua25vd24ub3JnIiwiYWxnIjoiSFMyNTYifQ
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