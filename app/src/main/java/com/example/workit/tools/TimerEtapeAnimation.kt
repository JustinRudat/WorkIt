package com.example.workit.tools

import android.view.animation.Animation
import android.view.animation.Transformation

/**
 * Created by JustinRudat on 06/03/2019.
 */
class TimerEtapeAnimation(private val circle: TimerView, newAngle: Int) : Animation() {


    private val oldAngle: Float
    private val newAngle: Float


    init {
        this.oldAngle = circle.angle
        this.newAngle = newAngle.toFloat()

    }

    override fun applyTransformation(interpolatedTime: Float, transformation: Transformation) {
        val angle = oldAngle + (newAngle - oldAngle) * interpolatedTime

        circle.angle = angle
        circle.requestLayout()
    }

}