package ru.syrzhn.domain

import java.time.LocalDate

fun String.toRussiaDate(): String  {
    val list = this.split("-")
    val year = list[0]
    val month = when  {
        list[1] == "01" -> "Января"
        list[1] == "02" -> "Февраль"
        list[1] == "03" -> "Марта"
        list[1] == "04" -> "Апреля"
        list[1] == "05" -> "Мая"
        list[1] == "06" -> "Июня"
        list[1] == "07" -> "Июля"
        list[1] == "08" -> "Августа"
        list[1] == "09" -> "Сентября"
        list[1] == "10" -> "Октября"
        list[1] == "11" -> "Ноября"
        list[1] == "12" -> "Декабря"
        else -> "Января"
    }
    val day = list[2]
    return "$day $month $year"
}