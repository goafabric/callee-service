package org.goafabric.calleeservice.extensions

import jakarta.servlet.http.HttpServletRequest
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.util.*

object UserContext {
    data class UserContextRecord(val tenantId: String, val organizationId: String, val userName: String) {
        fun toAdapterHeaderMap(): Map<String, String> {
            return java.util.Map.of(
                "X-TenantId", tenantId,
                "X-OrganizationId", organizationId,
                "X-Auth-Request-Preferred-Username", userName
            )
        }
    }

    private val CONTEXT: ThreadLocal<UserContextRecord> =
        ThreadLocal.withInitial { UserContextRecord("0", "0", "anonymous") }

    fun setContext(request: HttpServletRequest) {
        setContext(
            request.getHeader("X-TenantId"), request.getHeader("X-OrganizationId"),
            request.getHeader("X-Auth-Request-Preferred-Username"), request.getHeader("X-UserInfo")
        )
    }

    fun setContext(tenantId: String?, organizationId: String?, userName: String?, userInfo: String?) {
        CONTEXT.set(
            UserContextRecord(
                getValue(tenantId, "0"),
                getValue(organizationId, "0"),
                getValue(getUserNameFromUserInfo(userInfo), getValue(userName, "anonymous"))
            )
        )
    }

    fun removeContext() {
        CONTEXT.remove()
    }

    private fun getValue(value: String?, defaultValue: String): String {
        return value ?: defaultValue
    }

    var tenantId: String
        get() = CONTEXT.get().tenantId
        set(tenant) {
            CONTEXT.set(UserContextRecord(tenant, CONTEXT.get().organizationId, CONTEXT.get().userName))
        }

    val organizationId: String
        get() = CONTEXT.get().organizationId

    val userName: String
        get() = CONTEXT.get().userName

    val adapterHeaderMap: Map<String, String>
        get() = CONTEXT.get().toAdapterHeaderMap()

    private fun getUserNameFromUserInfo(userInfo: String?): String? {
        return if (userInfo != null) {
            val map: Map<String, Any>? = jacksonObjectMapper().readValue(Base64.getUrlDecoder().decode(userInfo))
            map?.get("preferred_username") as? String
        } else { null }
    }
}
