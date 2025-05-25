package com.github.saw47.features.login

import com.github.saw47.Test
import com.github.saw47.cache.InMemoryCache
import com.github.saw47.cache.TokenCache
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import java.util.UUID

fun Application.configureLoginRouting() {
    routing {
        get("/login") {
            val receive = call.receive(LoginReceiveRemote::class)
            if (InMemoryCache.userList.map { it.login }.contains(receive.login)) {
                val token = UUID.randomUUID().toString()
                InMemoryCache.token.add(TokenCache(login = receive.login, token = token))
                call.respond(LoginResponseRemote(token = token))
                return@get
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
            call.respond(Test(text = "Hello World"))
        }
    }
}