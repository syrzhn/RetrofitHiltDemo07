package ru.syrzhn.data.network

import jakarta.inject.Inject
import jakarta.inject.Singleton
import ru.syrzhn.domain.User

/**
 * The stub class represents a list of all
 * valid users to log into this application
 */
@Singleton
class UsersList @Inject constructor() {
    private val approvedUsers = arrayOf(
        User("qwerty@gmail.com", "123456"),
        User("Ivan.Ivanov@mail.ru", "123456"),
        User("admin@gmail.com", "qwerty"),
        User("admin","123456")
    )

    fun isValid(name: String, password: String): Boolean {
        return approvedUsers.find {
            it == User(name, password)
        } != null
    }

    override fun toString(): String {
        var str = "\nСписок зарегистрированных пользователей:\n\n"
        approvedUsers.forEach {
            str += "Имя: ${it.name} \t пароль:${it.password}\n\n"
        }
        return str
    }
}