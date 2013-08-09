dataSource {
    pooled = true
    driverClassName = "org.postgresql.Driver"
    username = "proteo"
    password = ""
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
//    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4

    multiTenancy = 'DATABASE'
    multi_tenant_connection_provider = 'com.genologics.utils.hibernate.MultiTenantConnectionProviderImpl'
    tenant_identifier_resolver = 'com.genologics.utils.hibernate.TenantResolver'
}

// environment specific settings
environments {
    development {
        dataSource {
//            dbCreate = "validate" // one of 'create', 'create-drop', 'update', 'validate', ''
            url = "jdbc:postgresql://localhost:5432/objectTestDB"
        }
        dataSource_tenant {
            pooled = true
            driverClassName = "org.postgresql.Driver"
            username = "proteo"
            password = ""
            url = "jdbc:postgresql://localhost:5432/tenant_dns"
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
        }
    }
    production {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:prodDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
            properties {
               maxActive = -1
               minEvictableIdleTimeMillis=1800000
               timeBetweenEvictionRunsMillis=1800000
               numTestsPerEvictionRun=3
               testOnBorrow=true
               testWhileIdle=true
               testOnReturn=false
               validationQuery="SELECT 1"
               jdbcInterceptors="ConnectionState"
            }
        }
    }
}
