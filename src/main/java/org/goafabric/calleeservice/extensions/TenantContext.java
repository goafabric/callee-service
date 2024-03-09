package org.goafabric.calleeservice.extensions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

public class TenantContext {
    private static final ThreadLocal<TenantContextRecord> tenantContext = ThreadLocal.withInitial(() -> new TenantContextRecord(null, null, null));

    record TenantContextRecord(String tenantId, String organizationId, String userName) {
        public TenantContextRecord(HttpServletRequest request) {
            this(request.getHeader("X-TenantId"), request.getHeader("X-OrganizationId"), request.getHeader("X-Auth-Request-Preferred-Username"));
        }
        public Map<String, String> toMap() {
            return Map.of("X-TenantId", tenantId, "X-OrganizationId", organizationId, "X-Auth-Request-Preferred-Username", userName);
        }
    }

    public static void setContext(HttpServletRequest request) {
        tenantContext.set(new TenantContextRecord(request));
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

    public static Map<String, String> getMap() {
        return tenantContext.get().toMap();
    }

    public static String getUserName() {
        return tenantContext.get().userName != null ? tenantContext.get().userName
                : SecurityContextHolder.getContext().getAuthentication() != null ? SecurityContextHolder.getContext().getAuthentication().getName() : "";
    }

}
