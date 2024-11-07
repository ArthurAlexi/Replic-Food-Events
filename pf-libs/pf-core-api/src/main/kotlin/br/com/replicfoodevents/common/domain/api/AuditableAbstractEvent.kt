package br.com.replicfoodevents.common.domain.api

import br.com.replicfoodevents.common.domain.api.model.AuditEntry

abstract class AuditableAbstractEvent(open val auditEntry: AuditEntry)