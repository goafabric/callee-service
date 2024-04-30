package org.goafabric.calleeservice.extensions

import org.assertj.core.api.Assertions
import org.goafabric.calleeservice.extensions.TenantContext.adapterHeaderMap
import org.goafabric.calleeservice.extensions.TenantContext.setContext
import org.junit.jupiter.api.Test

internal class TenantContextTest {
    @Test
    fun getTenantId() {
        setContext(null, null, null, null)
        Assertions.assertThat(TenantContext.tenantId).isEqualTo("0")
    }

    @Test
    fun getOrganizationId() {
        setContext(null, null, null, null)
        Assertions.assertThat(TenantContext.organizationId).isEqualTo("0")
    }

    @Test
    fun getUserName() {
        setContext(null, null, null, null)
        Assertions.assertThat(TenantContext.userName).isEqualTo("anonymous")
    }

    @Test
    fun setTenantId() {
        setContext(null, null, null, null)
        TenantContext.tenantId = "42"
        Assertions.assertThat(TenantContext.tenantId).isEqualTo("42")
    }

    @Test
    fun getUserInfo() {
        val userInfo = "eyJwcmVmZXJyZWRfdXNlcm5hbWUiOiJqb2huIGRvZSIsImFsZyI6IkhTMjU2In0" //eyJnZW5kZXIiOiJVTktOT1dOIiwiYmlydGhkYXRlIjoiMTk4MC0wMS0wMSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJ1cm46Z29hZmFicmljOmNsYWltczppbnN0aXR1dGlvbiI6W3siaWQiOiI4MmZhY2RkZS0xOTVjLTQ3OGUtODczYS0yM2MzNDBmZmYyZWQiLCJyb2xlcyI6WyJNRU1CRVIiLCJBRE1JTiJdLCJuYW1lIjoiVGVzdCJ9XSwibmFtZSI6IkpvbmggRG9lIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiam9obi5kb2VAdW5rbm93bi5vcmciLCJlbWFpbCI6ImpvaG4uZG9lQHVua25vd24ub3JnIiwiYWxnIjoiSFMyNTYifQ
        setContext("42", "44", "user", userInfo)
        Assertions.assertThat(TenantContext.userName).isEqualTo("john doe")
    }

    @Test
    fun getAll() {
        setContext("42", "44", "user", null)
        Assertions.assertThat(TenantContext.tenantId).isEqualTo("42")
        Assertions.assertThat(TenantContext.organizationId).isEqualTo("44")
        Assertions.assertThat(TenantContext.userName).isEqualTo("user")
        Assertions.assertThat(adapterHeaderMap).isNotNull().isNotEmpty()
    }
}