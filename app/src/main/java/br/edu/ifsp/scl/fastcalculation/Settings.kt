package br.edu.ifsp.scl.fastcalculation

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Settings(
    val playerName: String = "",
    val rounds: Int = 0,
    val roundInterval: Long = 0L
) : Parcelable
