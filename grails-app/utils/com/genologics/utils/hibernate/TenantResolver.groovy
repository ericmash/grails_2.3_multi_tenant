package com.genologics.utils.hibernate

import org.hibernate.context.spi.CurrentTenantIdentifierResolver

class TenantResolver implements CurrentTenantIdentifierResolver {
    @Override
    String resolveCurrentTenantIdentifier() {
        return 'doNotUse'
    }

    @Override
    boolean validateExistingCurrentSessions() {
        return true
    }
}
