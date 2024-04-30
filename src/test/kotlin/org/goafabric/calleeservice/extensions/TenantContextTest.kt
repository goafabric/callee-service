package org.goafabric.calleeservice.extensions

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

internal class TenantContextTest {
    @Test
    fun getTenantId() {
        TenantContext.setContext(null, null, null, null)
        assertThat(TenantContext.tenantId).isEqualTo("0")
    }

    @Test
    fun getOrganizationId() {
        TenantContext.setContext(null, null, null, null)
        assertThat(TenantContext.organizationId).isEqualTo("0")
    }

    @Test
    fun getUserName() {
        TenantContext.setContext(null, null, null, null)
        assertThat(TenantContext.userName).isEqualTo("anonymous")
    }

    @Test
    fun setTenantId() {
        TenantContext.setContext(null, null, null, null)
        TenantContext.tenantId = "42"
        assertThat(TenantContext.tenantId).isEqualTo("42")
    }

    @Test
    fun getUserInfo() {
        val userInfo = "eyJwcmVmZXJyZWRfdXNlcm5hbWUiOiJqb2huIGRvZSIsImFsZyI6IkhTMjU2In0" //eyJnZW5kZXIiOiJVTktOT1dOIiwiYmlydGhkYXRlIjoiMTk4MC0wMS0wMSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJ1cm46Z29hZmFicmljOmNsYWltczppbnN0aXR1dGlvbiI6W3siaWQiOiI4MmZhY2RkZS0xOTVjLTQ3OGUtODczYS0yM2MzNDBmZmYyZWQiLCJyb2xlcyI6WyJNRU1CRVIiLCJBRE1JTiJdLCJuYW1lIjoiVGVzdCJ9XSwibmFtZSI6IkpvbmggRG9lIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiam9obi5kb2VAdW5rbm93bi5vcmciLCJlbWFpbCI6ImpvaG4uZG9lQHVua25vd24ub3JnIiwiYWxnIjoiSFMyNTYifQ
        TenantContext.setContext("42", "44", "user", userInfo)
        assertThat(TenantContext.userName).isEqualTo("john doe")
    }

    @Test
    fun getAll() {
        TenantContext.setContext("42", "44", "user", null)
        assertThat(TenantContext.tenantId).isEqualTo("42")
        assertThat(TenantContext.organizationId).isEqualTo("44")
        assertThat(TenantContext.userName).isEqualTo("user")
        assertThat(TenantContext.adapterHeaderMap).isNotNull().isNotEmpty()
    }
}