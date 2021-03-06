package com.example.workit.tools

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

/**
 * Created by JustinRudat on 06/03/2019.
 */
class TimerView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    val paint: Paint
    val rect: RectF

    var angle: Float = 0.toFloat()

    init {

        val strokeWidth = 40

        paint = Paint()
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = strokeWidth.toFloat()
        //Circle color
        paint.color = Color.RED

        //size 200x200 example
        rect = RectF(
            strokeWidth.toFloat(),
            strokeWidth.toFloat(),
            (90 + strokeWidth).toFloat(),
            (90 + strokeWidth).toFloat()
        )

        //Initial Angle (optional, it can be zero)
        angle = 0f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawArc(rect, START_ANGLE_POINT.toFloat(), angle, false, paint)
    }

    companion object {

        private val START_ANGLE_POINT = 0
    }
}