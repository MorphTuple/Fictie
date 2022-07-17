package io.morphtuple.fictie.common

fun String.parseIntOrNullComma(): Int? = replace(",", "").toIntOrNull(radix = 10)
