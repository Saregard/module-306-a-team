package com.example.myquizgame.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Question(
    val responseCode: Int,
    val results: List<Result>
) : Parcelable
