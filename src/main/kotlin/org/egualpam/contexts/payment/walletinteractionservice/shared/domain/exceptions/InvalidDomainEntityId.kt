package org.egualpam.contexts.payment.walletinteractionservice.shared.domain.exceptions

class InvalidDomainEntityId(value: String) :
  RuntimeException("The provided id [$value] is invalid")
