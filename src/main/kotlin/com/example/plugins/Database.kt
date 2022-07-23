package com.example.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.util.Properties

const val ENV_DB_USER = "DB_USER"
const val ENV_DB_PASSWORD = "DB_PASSWORD"
const val ENV_DB_NAME = "DB_NAME"
const val ENV_DB_PORT = "DB_PORT"
const val ENV_DB_HOST = "DB_HOST"

fun configureHikariDataSource(
    user: String = System.getenv(ENV_DB_USER),
    password: String = System.getenv(ENV_DB_PASSWORD),
    dbName: String = System.getenv(ENV_DB_NAME),
    port: Int = System.getenv(ENV_DB_PORT).toInt(),
    host: String = System.getenv(ENV_DB_HOST),
): HikariDataSource {
    val config = HikariConfig(
        Properties().apply {
            setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource")
            setProperty("dataSource.user", user)
            setProperty("dataSource.password", password)
            setProperty("dataSource.databaseName", dbName)
            setProperty("dataSource.portNumber", port.toString())
            setProperty("dataSource.serverName", host)
        }
    )
    return HikariDataSource(config)
}