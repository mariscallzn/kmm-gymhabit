package com.andymariscal.gymhabit.inf

import kotlinx.datetime.*

fun now(): String = Clock.System.now().toString()

//TODO: WIP:
fun dateTimeFormat(
    datetime: String,
    timeZone: TimeZone = TimeZone.currentSystemDefault()
): String {
    return datetime.toInstant().toLocalDateTime(timeZone).toString()
}