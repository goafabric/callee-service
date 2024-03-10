package org.goafabric.calleeservice.extensions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

public class TenantContext {
    private static final ThreadLocal<TenantContextRecord> tenantContext = ThreadLocal.withInitial(() -> new TenantContextRecord(null, null, null));

    record TenantContextRecord(String tenantId, String organizationId, String userName) {
        public Map<String, String> toAdapterHeaderMap() {
            return Map.of("X-TenantId", tenantId, "X-OrganizationId", organizationId, "X-Auth-Request-Preferred-Username", userName);
        }
    }

    public static void setContext(HttpServletRequest request) {
        tenantContext.set(new TenantContextRecord(
                request.getHeader("X-TenantId"),
                request.getHeader("X-OrganizationId"),
                request.getHeader("X-Auth-Request-Preferred-Username")));
    }

    static void setContext(TenantContextRecord tenantContextRecord) {
        tenantContext.set(tenantContextRecord);
    }

    public static void removeContext() {
        tenantContext.remove();
    }

    public static void setTenantId(String tenant) {
        tenantContext.set(new TenantContextRecord(tenant, tenantContext.get().organizationId, tenantContext.get().userName));
    }

    public static String getTenantId() {
        return tenantContext.get().tenantId() != null ? tenantContext.get().tenantId() : "0";
    }

    public static String getOrganizationId() {
        return tenantContext.get().organizationId() != null ? tenantContext.get().organizationId() : "1";
    }

    public static Map<String, String> getAdapterHeaderMap() {
        return tenantContext.get().toAdapterHeaderMap();
    }

    public static String getUserName() {
        return (getAuthentication() != null) && !(getAuthentication().getName().equals("anonymousUser")) ? getAuthentication().getName()
                : tenantContext.get().userName != null ? tenantContext.get().userName : "anonymous";
    }

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }


}
