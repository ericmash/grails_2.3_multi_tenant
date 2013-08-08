package com.genologics.utils.hibernate

class CurrentTenantHolder {
    private static final ThreadLocal contextHolder = new ThreadLocal();

    static void setTenantId(String tenantId) {
        contextHolder.set(tenantId);
    }

    static String getTenantId() {
        return contextHolder.get()
    }
}
