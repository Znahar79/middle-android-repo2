package ru.yandex.praktikumchatapp.common

fun powerBaseTwo(power: Long): Long {
    return (1L shl power.toInt())
}