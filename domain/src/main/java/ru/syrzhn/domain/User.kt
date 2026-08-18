package ru.syrzhn.domain

data class User (val name: String, val password: String) {
    override fun equals(other: Any?): Boolean {
        if (other !is User)
            return false
        return name == other.name && password == other.password
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + password.hashCode()
        return result
    }
}