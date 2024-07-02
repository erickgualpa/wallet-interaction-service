# 💰 Wallet Interaction Service 💸

![CI/CD status](https://github.com/erickgualpa/wallet-interaction-service/actions/workflows/maven.yml/badge.svg)
[![](https://img.shields.io/badge/Spring%20Boot%20Version-3.3.1-blue)](/pom.xml)
[![](https://img.shields.io/badge/Java%20Version-21-blue)](/pom.xml)
[![](https://img.shields.io/badge/Kotlin%20Version-2.0.0-blue)](/pom.xml)

![wallet-interaction-service](etc/wallet-interaction-service.png)

🧪 Run tests
<br>

```shell script
./mvnw clean verify
```

📣 This project has been structured following a Hexagonal Architecture

[//]: # (Directory tree below was generated using 'tree -d -I target' command)

```
.
└── src
    ├── main
    │   ├── kotlin
    │   │   └── org
    │   │       └── egualpam
    │   │           └── contexts
    │   │               └── payment
    │   │                   └── walletinteractionservice
    │   │                       └── shared
    │   │                           └── adapters
    │   └── resources
    └── test
        ├── kotlin
        │   └── org
        │       └── egualpam
        │           └── contexts
        │               └── payment
        │                   └── walletinteractionservice
        │                       ├── health
        │                       └── shared
        │                           └── adapters
        └── resources
```
