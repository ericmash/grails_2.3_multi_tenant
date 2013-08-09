package grails_2_3_playground

import static org.springframework.http.HttpStatus.*

class TenantController {

    def tenantService

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def tenants = tenantService.list()
        respond tenants, model: [tenantInstanceCount: tenants.size()]
//        respond Tenant.list(params), model: [tenantInstanceCount: Tenant.count()]
    }
}

