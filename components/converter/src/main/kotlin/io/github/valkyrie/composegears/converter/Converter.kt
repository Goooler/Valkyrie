package io.github.valkyrie.composegears.converter

import io.github.composegears.valkyrie.generator.imagevector.ImageVectorGenerator
import io.github.composegears.valkyrie.generator.imagevector.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.parser.IconParser
import io.github.composegears.valkyrie.parser.isSvg
import io.github.composegears.valkyrie.parser.isXml
import java.io.IOException
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.createParentDirectories
import kotlin.io.path.isRegularFile
import kotlin.io.path.writeText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun List<Path>.filterFormats(): List<Path> {
    return filter { it.isRegularFile() && (it.isXml || it.isSvg) }
}

@Throws(IOException::class)
fun String.writeToPath(
    outputDirectory: String,
    fileName: String,
    createParents: Boolean = true,
) {
    val outPath = Path("$outputDirectory/$fileName.kt")
    if (createParents) {
        outPath.createParentDirectories()
    }
    outPath.writeText(this)
}

suspend fun List<BatchIcon>.writeToPath(
    packageName: String,
    outputDirectory: String,
    generatePreview: Boolean,
) = withContext(Dispatchers.IO) {
    filterIsInstance<BatchIcon.Valid>()
        .forEachIndexed { index, icon ->
            val iconPack = icon.iconPack
            val isNested = iconPack is IconPack.Nested
            val nestedPackName = if (isNested) icon.iconPack.currentNestedPack else ""
            val destDir = if (isNested) {
                "$outputDirectory/${iconPack.currentNestedPack.lowercase()}"
            } else {
                outputDirectory
            }

            val parserOutput = IconParser.toVector(icon.path)
            val vectorSpecOutput = ImageVectorGenerator.convert(
                vector = parserOutput.vector,
                kotlinName = icon.iconName.value,
                config = ImageVectorGeneratorConfig(
                    packageName = icon.iconPack.iconPackage,
                    packName = packageName,
                    nestedPackName = nestedPackName,
                    generatePreview = generatePreview,
                ),
            )

            vectorSpecOutput.content.writeToPath(
                outputDirectory = destDir,
                fileName = vectorSpecOutput.name,
                createParents = index == 0,
            )
        }
}
