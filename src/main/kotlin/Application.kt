package com.github.saw47

import com.github.saw47.features.login.configureLoginRouting
import com.github.saw47.features.register.configureRegisterRouting
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import org.jetbrains.exposed.v1.jdbc.Database

fun main() {
    Database.connect(url = "jdbc:postgresql://localhost:5432/testdb", driver = "org.postgresql.Driver", user = "postgres", password = "postgres")

    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureRouting()
    configureLoginRouting()
    configureRegisterRouting()
    configureSerialization()
}
