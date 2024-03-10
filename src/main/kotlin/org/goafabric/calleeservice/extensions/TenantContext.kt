package org.goafabric.calleeservice.extensions

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

object TenantContext {
    data class TenantContextRecord(val tenantId: String?, val organizationId: String?, val userName: String?) {
        fun toAdapterHeaderMap(): Map<String, String?> {
            return java.util.Map.of(
                "X-TenantId", tenantId,
                "X-OrganizationId", organizationId,
                "X-Auth-Request-Preferred-Username", userName
            )
        }
    }

    private val tenantContext: ThreadLocal<TenantContextRecord> =
        ThreadLocal.withInitial { TenantContextRecord(null, null, null) }

    fun setContext(request: HttpServletRequest) {
        tenantContext.set(
            TenantContextRecord(
                request.getHeader("X-TenantId"),
                request.getHeader("X-OrganizationId"),
                request.getHeader("X-Auth-Request-Preferred-Username")
            )
        )
    }

    fun setContext(tenantContextRecord: TenantContextRecord) {
        tenantContext.set(tenantContextRecord)
    }

    fun removeContext() {
        tenantContext.remove()
    }

    var tenantId: String
        get() = if (tenantContext.get().tenantId != null) tenantContext.get().tenantId!! else "0"
        set(tenant) {
            tenantContext.set(
                TenantContextRecord(
                    tenant,
                    tenantContext.get().organizationId,
                    tenantContext.get().userName
                )
            )
        }

    val organizationId: String
        get() = if (tenantContext.get().organizationId != null) tenantContext.get().organizationId!! else "0"

    val userName: String
        get() = if ((authentication != null) && authentication!!.name != "anonymousUser") authentication!!.name
        else (if (tenantContext.get().userName != null) tenantContext.get().userName else "anonymous")!!

    val adapterHeaderMap: Map<String, String?>
        get() = tenantContext.get().toAdapterHeaderMap()

    private val authentication: Authentication?
        get() = SecurityContextHolder.getContext().authentication
}
