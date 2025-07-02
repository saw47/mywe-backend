package com.github.saw47.features.login

import com.github.saw47.database.tokens.TokenDTO
import com.github.saw47.database.tokens.Tokens
import com.github.saw47.database.users.Users
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import java.util.UUID

class LoginController(private val call: ApplicationCall) {
    suspend fun performLogin() {
        val receive = call.receive<LoginReceiveRemote>()
        val userDTO = Users.fetchUser(receive.login)

        println("login -- ${receive.login} ;; userDTO --  $userDTO")

        if (userDTO == null) {
            call.respond(HttpStatusCode.BadRequest, "User not found")
        } else {
            if (userDTO.password == receive.password) {
                val token = UUID.randomUUID().toString()

                Tokens.insert(
                    TokenDTO(
                        rowId = UUID.randomUUID().toString(),
                        login = receive.login,
                        token = token
                    )
                )

                call.respond(LoginResponseRemote(token = token))
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid password")
            }
        }
    }
}