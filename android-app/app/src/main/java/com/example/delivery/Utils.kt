package com.example.delivery

fun Boolean.toInt() = if(this) 1 else 0

fun Double.format(digits: Int) = "%.${digits}f".format(this).toDouble()