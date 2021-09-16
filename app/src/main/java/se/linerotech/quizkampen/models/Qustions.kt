package com.example.myquizgame.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Qustions(
    val response_code: Int,
    val results: List<Result>
):Parcelable