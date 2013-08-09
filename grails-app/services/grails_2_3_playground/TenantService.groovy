package grails_2_3_playground

import groovy.sql.Sql

class TenantService {

    static transactional = false

    javax.sql.DataSource dataSource_tenant

    List<Tenant> list() {
        def dataRows = new Sql(dataSource_tenant).rows('''
            SELECT * FROM tenant_dns_map
        ''')

        def tenants = dataRows.collect([]) {
            new Tenant(tenantId: it.tenantid, serverName: it.servername, database: it.database)
        }

        return tenants
    }

    Tenant getTenant(String tenantId) {
        def data = new Sql(dataSource_tenant).firstRow('''
            SELECT * FROM tenant_dns_map
            WHERE tenantid = ?
        ''', [tenantId])

        return new Tenant(tenantId: data.tenantid, serverName: data.servername, database: data.database)
    }

    Tenant findTenantByServerName(String serverName) {
        def data = new Sql(dataSource_tenant).firstRow('''
            SELECT * FROM tenant_dns_map
            WHERE servername = ?
        ''', [serverName])

        return new Tenant(tenantId: data.tenantid, serverName: data.servername, database: data.database)
    }
}
