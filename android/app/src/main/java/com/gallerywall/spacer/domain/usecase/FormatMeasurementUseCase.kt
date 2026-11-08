package com.gallerywall.spacer.domain.usecase

import com.gallerywall.spacer.domain.model.UnitSystem
import kotlin.math.abs
import kotlin.math.round

class FormatMeasurementUseCase {

    operator fun invoke(valueCm: Float, unit: UnitSystem): String = format(convert(valueCm, unit))

    fun whole(valueCm: Float, unit: UnitSystem): Int = round(convert(valueCm, unit)).toInt()

    private fun convert(valueCm: Float, unit: UnitSystem): Float = when (unit) {
        UnitSystem.CM -> valueCm
        UnitSystem.INCH -> valueCm / CM_PER_INCH
    }

    private fun format(value: Float): String {
        val rounded = round(value * 10f) / 10f
        val whole = rounded.toInt()
        val frac = round(abs(rounded - whole) * 10f).toInt()
        return if (frac == 0) whole.toString() else whole.toString() + "." + frac.toString()
    }

    private companion object {
        const val CM_PER_INCH = 2.54f
    }
}
