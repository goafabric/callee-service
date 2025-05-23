package org.goafabric.calleeservice.extensions

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

internal class TenantContextTest {
    @Test
    fun getTenantId() {
        UserContext.setContext(null, null, null, null)
        assertThat(UserContext.tenantId).isEqualTo("0")
    }

    @Test
    fun getOrganizationId() {
        UserContext.setContext(null, null, null, null)
        assertThat(UserContext.organizationId).isEqualTo("0")
    }

    @Test
    fun getUserName() {
        UserContext.setContext(null, null, null, null)
        assertThat(UserContext.userName).isEqualTo("anonymous")
    }

    @Test
    fun setTenantId() {
        UserContext.setContext(null, null, null, null)
        UserContext.tenantId = "42"
        assertThat(UserContext.tenantId).isEqualTo("42")
    }

    @Test
    fun getUserInfo() {
        val userInfo = "eyJwcmVmZXJyZWRfdXNlcm5hbWUiOiJqb2huIGRvZSIsImFsZyI6IkhTMjU2In0" //eyJnZW5kZXIiOiJVTktOT1dOIiwiYmlydGhkYXRlIjoiMTk4MC0wMS0wMSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJ1cm46Z29hZmFicmljOmNsYWltczppbnN0aXR1dGlvbiI6W3siaWQiOiI4MmZhY2RkZS0xOTVjLTQ3OGUtODczYS0yM2MzNDBmZmYyZWQiLCJyb2xlcyI6WyJNRU1CRVIiLCJBRE1JTiJdLCJuYW1lIjoiVGVzdCJ9XSwibmFtZSI6IkpvbmggRG9lIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiam9obi5kb2VAdW5rbm93bi5vcmciLCJlbWFpbCI6ImpvaG4uZG9lQHVua25vd24ub3JnIiwiYWxnIjoiSFMyNTYifQ
        UserContext.setContext("42", "44", "user", userInfo)
        assertThat(UserContext.userName).isEqualTo("john doe")
    }

    @Test
    fun getAll() {
        UserContext.setContext("42", "44", "user", null)
        assertThat(UserContext.tenantId).isEqualTo("42")
        assertThat(UserContext.organizationId).isEqualTo("44")
        assertThat(UserContext.userName).isEqualTo("user")
        assertThat(UserContext.adapterHeaderMap).isNotNull().isNotEmpty()
    }
}