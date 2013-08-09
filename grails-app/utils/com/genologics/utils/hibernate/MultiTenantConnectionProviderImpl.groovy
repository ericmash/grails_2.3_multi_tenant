package com.genologics.utils.hibernate

import grails_2_3_playground.Tenant
import org.hibernate.cfg.AvailableSettings
import org.hibernate.service.jdbc.connections.internal.DriverManagerConnectionProviderImpl
import org.hibernate.service.jdbc.connections.spi.AbstractMultiTenantConnectionProvider
import org.hibernate.service.jdbc.connections.spi.ConnectionProvider

class MultiTenantConnectionProviderImpl extends AbstractMultiTenantConnectionProvider {

    private Map<String, ConnectionProvider> tenantIdToConnectionProvider = [:]

    @Override
    protected ConnectionProvider getAnyConnectionProvider() {
        return selectConnectionProvider('default')
    }

    @Override
    protected ConnectionProvider selectConnectionProvider(String tenantId) {
        // instead of using tenant id that is stored in each hibernate 4 session
        // use the tenant id in ThreadLocal variable which is resolved from DNS
        Tenant tenant = CurrentTenantHolder.tenant

        // when server starts, it initializes session factory which requires a default connection provider
        // this default connection provider shouldn't be used most of the time
        if (!tenant) {
            return createConnectionProvider('objectTestDB')
        }

        ConnectionProvider connectionProvider

        // use the following line for conection provider caching
        // if cache is enabled, database connection can be added to new tenant, but cannot be modified
//        ConnectionProvider connectionProvider = tenantIdToConnectionProvider[tenant.tenantId]

        if (!connectionProvider) {
            connectionProvider = createConnectionProvider(tenant.database)
            // disable this line so that databse can be changed on the fly
            tenantIdToConnectionProvider[tenant.tenantId] = connectionProvider
        }

        return connectionProvider
    }

    private ConnectionProvider createConnectionProvider(String databaseName) {
        ConnectionProvider connectionProvider = new DriverManagerConnectionProviderImpl()

        connectionProvider.configure([
                (AvailableSettings.DRIVER): 'org.postgresql.Driver',
                (AvailableSettings.AUTOCOMMIT): false,
                (AvailableSettings.URL): "jdbc:postgresql://localhost:5432/$databaseName" as String
        ])

        return connectionProvider
    }
}
