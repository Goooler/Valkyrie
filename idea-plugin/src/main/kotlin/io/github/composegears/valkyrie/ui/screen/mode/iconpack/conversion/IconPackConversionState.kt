package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion

import io.github.valkyrie.composegears.converter.BatchIcon

sealed interface IconPackConversionState {

    data object IconsPickering : IconPackConversionState

    sealed interface BatchProcessing : IconPackConversionState {

        data class IconPackCreationState(
            val icons: List<BatchIcon>,
            val exportEnabled: Boolean,
        ) : BatchProcessing

        data object ImportValidationState : BatchProcessing
        data object ExportingState : BatchProcessing
    }
}
