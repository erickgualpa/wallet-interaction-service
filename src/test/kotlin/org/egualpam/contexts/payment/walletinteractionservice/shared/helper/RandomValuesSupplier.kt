package org.egualpam.contexts.payment.walletinteractionservice.shared.helper

import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric

class RandomValuesSupplier {
  companion object {
    fun getRandomAlphabetic(length: Int): String = randomAlphabetic(length)
    fun getRandomAlphanumeric(length: Int): String = randomAlphanumeric(length)
  }
}
