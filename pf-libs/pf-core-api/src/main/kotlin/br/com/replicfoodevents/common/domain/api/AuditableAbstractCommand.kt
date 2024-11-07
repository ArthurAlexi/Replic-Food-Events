package br.com.replicfoodevents.common.domain.api

import br.com.replicfoodevents.common.domain.api.model.AuditEntry

abstract class AuditableAbstractCommand(open val auditEntry: AuditEntry)