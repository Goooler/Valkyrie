package io.github.composegears.valkyrie.parser

import androidx.compose.material.icons.generator.Icon
import androidx.compose.material.icons.generator.IconParser
import androidx.compose.material.icons.generator.vector.Vector
import com.android.ide.common.vectordrawable.Svg2Vector
import io.github.composegears.valkyrie.parser.IconType.SVG
import io.github.composegears.valkyrie.parser.IconType.XML
import java.nio.file.Path
import kotlin.io.path.createTempFile
import kotlin.io.path.extension
import kotlin.io.path.name
import kotlin.io.path.outputStream
import kotlin.io.path.readText

data class IconParserOutput(
    val vector: Vector,
    val kotlinName: String,
)

object IconParser {
    private val regex = "[^a-zA-Z0-9\\-_ ]".toRegex()

    fun svgToXml(input: Path): String {
        val output = createTempFile().outputStream()
        return Svg2Vector.parseSvgToXml(input, output)
    }

    @Throws(IllegalStateException::class)
    fun toVector(path: Path): IconParserOutput {
        val iconType = IconType.from(path.extension) ?: error("File not SVG or XML")

        val fileName = getIconName(fileName = path.name)
        val icon = when (iconType) {
            SVG -> {
                val xml = svgToXml(path)
                Icon(fileContent = xml)
            }
            XML -> Icon(fileContent = path.readText())
        }

        return IconParserOutput(
            vector = IconParser(icon).parse(),
            kotlinName = fileName,
        )
    }

    fun getIconName(fileName: String) = fileName
        .removePrefix("-")
        .removePrefix("_")
        .removeSuffix(".svg")
        .removeSuffix(".xml")
        .removePrefix("ic_")
        .removePrefix("ic-")
        .replace(regex, "_")
        .split("_", "-")
        .joinToString(separator = "") { it.lowercase().capitalized() }

    private fun String.removePrefix(prefix: CharSequence, ignoreCase: Boolean = true): String {
        if (startsWith(prefix, ignoreCase)) {
            return substring(prefix.length)
        }
        return this
    }

    private fun String.removeSuffix(suffix: CharSequence, ignoreCase: Boolean = true): String {
        if (endsWith(suffix, ignoreCase)) {
            return substring(0, length - suffix.length)
        }
        return this
    }
}
