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
val strokeFactor : Int = 90
val backColor : Int = Color.parseColor("#BDBDBD")
val delay : Long = 20

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
fun Float.sinify() : Float = Math.sin(this * Math.PI).toFloat()

fun Canvas.drawHCreatorBlock(i : Int, scale : Float, w : Float, h : Float, paint : Paint) {
    val size : Float = Math.min(w, h) / sizeFactor
    val sf : Float = scale.sinify()
    val sfi : Float = sf.divideScale(i, 2)
    save()
    scale(1f - 2 * i, 1f)
    for (j in 0..1) {
        val sf1 : Float = sfi.divideScale(0, 3)
        val sf2 : Float = sfi.divideScale(1, 3)
        val sf3 : Float = sfi.divideScale(2, 3)
        val uSize : Float = (size * sf1)
        val x : Float = (w / 2 - size) * sf2
        val y : Float = (h / 2 - size) * sf3
        save()
        scale(1f, 1f - 2 * i)
        drawLine(0f, 0f, x, 0f, paint)
        save()
        translate(x, 0f)
        drawLine(0f, 0f, 0f, y, paint)
        save()
        translate(0f, y)
        drawRect(RectF(-uSize / 2, -uSize / 2, uSize / 2, uSize / 2), paint)
        restore()
        restore()
        restore()
    }
    restore()
}

fun Canvas.drawHCreatorBlocks(scale : Float, w : Float, h : Float, paint : Paint) {
    save()
    translate(w / 2, h / 2)
    for (j in 0..1) {
        drawHCreatorBlock(j, scale, w, h, paint)
    }
    restore()
}

fun Canvas.drawHCBNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = Color.parseColor(colors[i])
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawHCreatorBlocks(scale, w, h, paint)
}

class HCreatorBlockView(ctx : Context) : View(ctx) {

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += scGap * dir
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(delay)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class HCBNode(var i : Int, val state : State = State()) {

        private var next : HCBNode? = null
        private var prev : HCBNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < colors.size - 1) {
                next = HCBNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawHCBNode(i, state.scale, paint)
        }

        fun update(cb : (Float) -> Unit) {
            state.update(cb)
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : HCBNode {
            var curr : HCBNode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }
    }

}