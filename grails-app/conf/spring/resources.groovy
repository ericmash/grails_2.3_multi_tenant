// Place your Spring DSL code here
beans = {
    transactionManager(org.springframework.orm.hibernate4.HibernateTransactionManager) {
        autodetectDataSource = false
        sessionFactory = ref("sessionFactory")
    }
}
