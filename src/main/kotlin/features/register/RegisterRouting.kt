package com.github.saw47.features.register

import com.github.saw47.Test
import com.github.saw47.cache.InMemoryCache
import com.github.saw47.cache.TokenCache
import com.github.saw47.features.login.LoginReceiveRemote
import com.github.saw47.features.login.LoginResponseRemote
import com.github.saw47.utils.isValidEmail
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import java.util.UUID

fun Application.configureRegisterRouting() {
    routing {
        post("/register") {
            val registerController = RegisterController(call)
            registerController.registerNewUser()


        }
    }
}