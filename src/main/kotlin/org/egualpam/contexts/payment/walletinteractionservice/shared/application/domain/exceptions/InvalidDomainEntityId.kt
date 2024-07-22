package org.egualpam.contexts.payment.walletinteractionservice.shared.application.domain.exceptions

class InvalidDomainEntityId(value: String) :
  RuntimeException("The provided id [$value] is invalid")
