package com.anwesh.uiprojects.hcreatorlineview

/**
 * Created by anweshmishra on 24/07/20.
 */

import android.view.View
import android.view.MotionEvent
import android.content.Context
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RectF
import android.app.Activity

val colors : Array<String> = arrayOf("#F44336", "#4CAF50", "#3F51B5", "#FF5722", "#03A9F4")
val parts : Int = 3
val scGap : Float = 0.02f / parts
val sizeFactor : Float = 6f
val backColor : Int = Color.parseColor("#BDBDBD")
val delay : Long = 20

