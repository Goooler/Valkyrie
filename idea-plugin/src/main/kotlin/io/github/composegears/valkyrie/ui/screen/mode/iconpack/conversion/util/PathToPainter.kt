package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.util

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toPainter
import com.android.ide.common.vectordrawable.VdPreview
import io.github.composegears.valkyrie.parser.IconParser
import io.github.composegears.valkyrie.parser.isSvg
import io.github.composegears.valkyrie.parser.isXml
import java.nio.file.Path
import kotlin.io.path.readText

fun Path.toPainterOrNull(imageScale: Double = 5.0): Painter? = when {
    isSvg -> svgToPainter(imageScale)
    isXml -> xmlToPainter(imageScale)
    else -> null
}

private fun Path.svgToPainter(imageScale: Double): Painter? {
    return runCatching {
        VdPreview.getPreviewFromVectorXml(
            VdPreview.TargetSize.createFromScale(imageScale),
            IconParser.svgToXml(this),
            StringBuilder(),
        ).toPainter()
    }.getOrNull()
}

private fun Path.xmlToPainter(imageScale: Double): Painter? = runCatching {
    VdPreview.getPreviewFromVectorXml(
        VdPreview.TargetSize.createFromScale(imageScale),
        this.readText(),
        StringBuilder(),
    ).toPainter()
}.getOrNull()
