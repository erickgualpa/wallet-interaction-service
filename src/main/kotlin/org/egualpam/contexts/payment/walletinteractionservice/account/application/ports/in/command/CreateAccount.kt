package org.egualpam.contexts.payment.walletinteractionservice.account.application.ports.`in`.command

class CreateAccount {
  fun execute(command: CreateAccountCommand) {

  }
}

data class CreateAccountCommand(
  val accountId: String,
  val accountCurrency: String
)
