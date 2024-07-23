package io.github.composegears.valkyrie.parser

import java.util.Locale

fun String.capitalized(): String = replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
}
