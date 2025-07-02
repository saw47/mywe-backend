package com.github.saw47.database.users

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.core.SqlExpressionBuilder.eq
import org.jetbrains.exposed.v1.jdbc.selectAll

object Users: Table("users") {
    private val login = varchar("login", 25)
    private val password = varchar("password", 25)
    private val username = varchar("username", 50)
    private val email = varchar("email", 50)

    fun insert(userDTO: UserDTO) {
        transaction {
            Users.insert {
                it[login] = userDTO.login
                it[password] = userDTO.password
                it[username] = userDTO.userName
                it[email] = userDTO.email ?: ""
            }
        }
    }

    fun fetchUser(login: String): UserDTO? {
        return try {
            transaction {
                println("login - $login")
                val userModelD = Users.select(column = Users.login).where { Users.login.eq(login) }.forEach { println(it)}
                println("userModel - $userModelD")

                val userModel = Users.selectAll().where { Users.login.eq(login) }.single()
                UserDTO(
                    login = userModel[Users.login],
                    password = userModel[password],
                    userName = userModel[username],
                    email = userModel[email]
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}