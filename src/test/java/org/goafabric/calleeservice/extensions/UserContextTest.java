package org.goafabric.calleeservice.extensions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserContextTest {

    @Test
    void getTenantId() {
        UserContext.setContext(null, null, null, null);
        assertThat(UserContext.getTenantId()).isEqualTo("0");
    }

    @Test
    void getOrganizationId() {
        UserContext.setContext(null, null, null, null);
        assertThat(UserContext.getOrganizationId()).isEqualTo("0");
    }

    @Test
    void getUserName() {
        UserContext.setContext(null, null, null, null);
        assertThat(UserContext.getUserName()).isEqualTo("anonymous");
    }
    
    @Test
    void setTenantId() {
        UserContext.setContext(null, null, null, null);
        UserContext.setTenantId("42");
        assertThat(UserContext.getTenantId()).isEqualTo("42");
    }

    @Test
    void getUserNameFromUserINfo() {
        var userInfo = "eyJwcmVmZXJyZWRfdXNlcm5hbWUiOiJqb2huIGRvZSIsImFsZyI6IkhTMjU2In0"; //eyJnZW5kZXIiOiJVTktOT1dOIiwiYmlydGhkYXRlIjoiMTk4MC0wMS0wMSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJ1cm46Z29hZmFicmljOmNsYWltczppbnN0aXR1dGlvbiI6W3siaWQiOiI4MmZhY2RkZS0xOTVjLTQ3OGUtODczYS0yM2MzNDBmZmYyZWQiLCJyb2xlcyI6WyJNRU1CRVIiLCJBRE1JTiJdLCJuYW1lIjoiVGVzdCJ9XSwibmFtZSI6IkpvbmggRG9lIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiam9obi5kb2VAdW5rbm93bi5vcmciLCJlbWFpbCI6ImpvaG4uZG9lQHVua25vd24ub3JnIiwiYWxnIjoiSFMyNTYifQ
        UserContext.setContext("42", "44", "user", userInfo);
        assertThat(UserContext.getUserName()).isEqualTo("john doe");
    }

    @Test
    void getAll() {
        UserContext.setContext("42", "44", "user", null);
        assertThat(UserContext.getTenantId()).isEqualTo("42");
        assertThat(UserContext.getOrganizationId()).isEqualTo("44");
        assertThat(UserContext.getUserName()).isEqualTo("user");
        assertThat(UserContext.getAdapterHeaderMap()).isNotNull().isNotEmpty();
    }

}