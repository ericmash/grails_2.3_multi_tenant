package com.genologics.utils.hibernate

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
        String resolvedTenantId = CurrentTenantHolder.tenantId
        ConnectionProvider connectionProvider = tenantIdToConnectionProvider[resolvedTenantId]

        if (!connectionProvider) {
            switch (resolvedTenantId) {
                case 'tenant1':
                    connectionProvider = createConnectionProvider('multiTenant1')
                    break
                case 'default':
                default:
                    connectionProvider = createConnectionProvider('objectTestDB')
                    break
            }

            tenantIdToConnectionProvider[resolvedTenantId] = connectionProvider
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
