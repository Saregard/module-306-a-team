package com.example.myquizgame.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Result(
    val category: String,
    val correctAnswer: String,
    val difficulty: String,
    val incorrectAnswers: List<String>,
    val question: String,
    val type: String
) : Parcelable
