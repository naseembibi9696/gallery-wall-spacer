package com.gallerywall.spacer.domain.model

sealed interface ArrangementIssue {

    data object None : ArrangementIssue

    data object TooWide : ArrangementIssue

    data object AboveCeiling : ArrangementIssue

    data object OverFurniture : ArrangementIssue

    data class NailOffWall(val frameId: Int) : ArrangementIssue
}
