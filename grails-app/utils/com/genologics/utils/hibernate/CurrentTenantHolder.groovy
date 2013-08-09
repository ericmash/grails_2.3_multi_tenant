package com.genologics.utils.hibernate

import grails_2_3_playground.Tenant

class CurrentTenantHolder {
    private static final ThreadLocal contextHolder = new ThreadLocal();

    static void setTenant(Tenant tenant) {
        contextHolder.set(tenant);
    }

    static Tenant getTenant() {
        return contextHolder.get() as Tenant
    }
}
