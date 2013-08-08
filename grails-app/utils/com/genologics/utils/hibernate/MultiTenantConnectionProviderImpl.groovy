package com.genologics.utils.hibernate

import org.hibernate.cfg.AvailableSettings
import org.hibernate.service.UnknownUnwrapTypeException
import org.hibernate.service.jdbc.connections.internal.DriverManagerConnectionProviderImpl
import org.hibernate.service.jdbc.connections.spi.AbstractMultiTenantConnectionProvider
import org.hibernate.service.jdbc.connections.spi.ConnectionProvider
import org.hibernate.service.jdbc.connections.spi.MultiTenantConnectionProvider

import java.sql.Connection
import java.sql.SQLException

class MultiTenantConnectionProviderImpl extends AbstractMultiTenantConnectionProvider {

    private Map<String, ConnectionProvider> tenantIdToConnectionProvider = [:]

    @Override
    protected ConnectionProvider getAnyConnectionProvider() {
        return selectConnectionProvider('default')
    }

    @Override
    protected ConnectionProvider selectConnectionProvider(String tenantId) {
        ConnectionProvider connectionProvider = tenantIdToConnectionProvider[tenantId]

        if (!connectionProvider) {
            connectionProvider = new DriverManagerConnectionProviderImpl();
            Map configuration = [
                    (AvailableSettings.DRIVER): 'org.postgresql.Driver',
                    (AvailableSettings.AUTOCOMMIT): false
            ]

            // instead of using tenant id that is stored in each hibernate 4 session
            // use the tenant id in ThreadLocal variable which is resolved from DNS
            switch (CurrentTenantHolder.tenantId) {
                case 'tenant1':
                    configuration[AvailableSettings.URL] = 'jdbc:postgresql://localhost:5432/multiTenant1'
                    break
                default:
                    configuration[AvailableSettings.URL] = 'jdbc:postgresql://localhost:5432/objectTestDB'
            }

            connectionProvider.configure(configuration)
        }

        return connectionProvider
    }
}
