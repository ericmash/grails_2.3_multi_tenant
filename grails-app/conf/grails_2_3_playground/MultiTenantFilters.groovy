package grails_2_3_playground

import com.genologics.utils.hibernate.CurrentTenantHolder

class MultiTenantFilters {

    def filters = {
        allURIs(uri: '/**') {
            before = {
                ThreadLocal contextHolder = new ThreadLocal()
                switch (request.serverName) {
                    case '127.0.0.1':
                        CurrentTenantHolder.setTenantId('tenant1')
                        break;
                    default:
                        CurrentTenantHolder.setTenantId('default')
                }
            }
        }
    }
}
