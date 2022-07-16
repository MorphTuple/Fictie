package io.morphtuple.fictie

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

fun Int?.toCommaString() = "%,d".format(this)

fun InputStream.readAssetString(): String {
    val reader = BufferedReader(InputStreamReader(this, "UTF-8"))

    val str = reader.readText()

    reader.close()

    return str
}