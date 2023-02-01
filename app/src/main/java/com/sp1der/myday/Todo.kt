package com.sp1der.myday

data class Todo(
    val title: String,
    var isChecked: Boolean = false,
    val color:String,
    val info:String = "无"
)