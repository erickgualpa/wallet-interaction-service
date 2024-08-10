package org.egualpam.contexts.payment.walletinteractionservice.shared.adapters.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@Configuration
@EnableJdbcRepositories(
    "org.egualpam.contexts.payment.walletinteractionservice.wallet.adapters.out.shared.springdatajdbc",
)
class SpringDataJdbcConfiguration
