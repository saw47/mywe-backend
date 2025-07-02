package com.github.saw47.features.register

import com.github.saw47.database.tokens.TokenDTO
import com.github.saw47.database.tokens.Tokens
import com.github.saw47.database.users.UserDTO
import com.github.saw47.database.users.Users
import com.github.saw47.utils.isValidEmail
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import org.jetbrains.exposed.v1.exceptions.ExposedSQLException
import java.util.UUID

class RegisterController(private val call: ApplicationCall) {
    suspend fun registerNewUser() {
        val registerReceiveRemote = call.receive<RegisterReceiveRemote>()
        if (!registerReceiveRemote.email.isValidEmail()) {
            call.respond(HttpStatusCode.BadRequest, "Email is not valid")
        }

        val userDTO = Users.fetchUser(registerReceiveRemote.login)

        if (userDTO != null) {
            call.respond(HttpStatusCode.Conflict, "User already exists")
        } else {
            val token = UUID.randomUUID().toString()

            try {
                Users.insert(
                    UserDTO(
                        login = registerReceiveRemote.login,
                        password = registerReceiveRemote.password,
                        email = registerReceiveRemote.email,
                        userName = ""
                    )
                )
            } catch (e: ExposedSQLException) {
                call.respond(HttpStatusCode.Conflict, "User already exists")
            }


            Tokens.insert(
                TokenDTO(
                    rowId = UUID.randomUUID().toString(),
                    login = registerReceiveRemote.login,
                    token = token
                )
            )

            call.respond(RegisterResponseRemote(token = token))
        }
    }
}