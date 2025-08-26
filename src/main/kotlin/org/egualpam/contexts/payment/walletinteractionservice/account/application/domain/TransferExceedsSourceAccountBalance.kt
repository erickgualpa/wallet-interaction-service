package org.egualpam.contexts.payment.walletinteractionservice.account.application.domain

class TransferExceedsSourceAccountBalance :
  RuntimeException("Transfer exceeds source account balance")
