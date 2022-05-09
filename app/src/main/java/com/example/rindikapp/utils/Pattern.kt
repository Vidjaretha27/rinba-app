package com.example.rindikapp.utils

object Pattern {

    // Level Beginner Pattern
    private val BEGINNER_1 = arrayListOf(6, 5, 4, 3, 4, 5, 6, 7, 5, 6)
    private val BEGINNER_2 = arrayListOf(11, 10, 9, 8, 9, 10, 11, 10, 10, 11)
    private val BEGINNER_3 = arrayListOf(7, 8, 7, 6, 7, 6, 5, 4, 5, 6, 5)
    private val BEGINNER_4 = arrayListOf(10, 11, 10, 11, 10, 11, 10, 9, 10, 11, 10)

    val BEGINNER_PATTERN = arrayListOf(
        BEGINNER_1,
        BEGINNER_2,
        BEGINNER_3,
        BEGINNER_4,
    )

    // Level Advanced Pattern
    private val ADVANCED_1 = arrayListOf(
        arrayListOf(6,  5,  4, 3, 4, 5,  6,  7,  5,  6),
        arrayListOf(11, 10, 9, 8, 9, 10, 11, 10, 10, 11)
    )

    private val ADVANCED_2 = arrayListOf(
        arrayListOf(7,  8,  7,  6,  7,  6,  5,  4, 5,  6,  5),
        arrayListOf(10, 11, 10, 11, 10, 11, 10, 9, 10, 11, 10)
    )

    val ADVANCED_PATTERN = arrayListOf(
        ADVANCED_1,
        ADVANCED_2
    )
}

//fun main() {
//    val level = 1
//    for (i in 0 until Pattern.BEGINNER_PATTERN[level - 1].size) {
//        println(Pattern.BEGINNER_PATTERN[level - 1][i])
//    }
//}