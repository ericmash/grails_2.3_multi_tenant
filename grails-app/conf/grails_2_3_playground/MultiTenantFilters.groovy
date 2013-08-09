package grails_2_3_playground

import com.genologics.utils.hibernate.CurrentTenantHolder

class MultiTenantFilters {

    def tenantService

    def filters = {
        allURIs(uri: '/**') {
            before = {
                Tenant tenant = tenantService.findTenantByServerName(request.serverName)
                CurrentTenantHolder.setTenant(tenant)
            }
        }
    }
}
