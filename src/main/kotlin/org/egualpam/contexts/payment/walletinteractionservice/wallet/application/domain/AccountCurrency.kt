package org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain

import org.egualpam.contexts.payment.walletinteractionservice.wallet.application.domain.exceptions.AccountCurrencyIsNotSupported

data class AccountCurrency(val value: String) {
  private enum class SupportedCurrencies {
    EUR;

    companion object {
      fun from(value: String): SupportedCurrencies? {
        return entries.firstOrNull { it.name == value }
      }
    }
  }

  init {
    value.let {
      SupportedCurrencies.from(it)
    } ?: throw AccountCurrencyIsNotSupported(value)
  }
}
