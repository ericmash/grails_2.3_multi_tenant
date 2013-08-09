import org.codehaus.groovy.grails.orm.hibernate.ConfigurableLocalSessionFactoryBean

// Place your Spring DSL code here
beans = {
    transactionManager(org.springframework.orm.hibernate4.HibernateTransactionManager) {
        autodetectDataSource = false
        sessionFactory = ref("sessionFactory")
    }
    transactionManager_tenant(org.springframework.orm.hibernate4.HibernateTransactionManager) {
        autodetectDataSource = false
        sessionFactory = ref("sessionFactory")
    }
}
