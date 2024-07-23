package io.github.valkyrie.composegears.converter

import java.nio.file.Path

@JvmInline
value class IconName(val value: String)

sealed interface IconPack {
    val iconPackage: String
    val currentNestedPack: String

    data class Single(
        override val iconPackage: String,
        val iconPackName: String,
    ) : IconPack {
        override val currentNestedPack = ""
    }

    data class Nested(
        override val iconPackage: String,
        val iconPackName: String,
        val nestedPacks: List<String>,
        override val currentNestedPack: String,
    ) : IconPack
}

sealed interface BatchIcon {
    val iconName: IconName
    val extension: String

    data class Broken(
        override val iconName: IconName,
        override val extension: String,
    ) : BatchIcon

    data class Valid(
        val iconPack: IconPack,
        override val iconName: IconName,
        override val extension: String,
        val path: Path,
    ) : BatchIcon
}
