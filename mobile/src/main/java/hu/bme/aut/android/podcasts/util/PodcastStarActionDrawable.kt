package hu.bme.aut.android.podcasts.util

import android.content.Context
import android.graphics.Canvas
import kotlin.math.hypot
import kotlin.math.sin

class PodcastStarActionDrawable(context: Context) : PodcastActionDrawable(context) {

    override fun update() {
        circle.set(
            bounds.left.toFloat(),
            bounds.top.toFloat(),
            bounds.right.toFloat(),
            bounds.bottom.toFloat()
        )
        // Get the longest visible distance at which the circle will be displayed (the hypotenuse of
        // the triangle from the center of the icon, to the furthest side of the rect, to the top
        // corner of the rect.
        cr = hypot(circle.right, (circle.height() / 2F))

        callback?.invalidateDrawable(this)
    }

    override fun draw(canvas: Canvas) {
        // Draw the circular reveal background.
        canvas.drawCircle(
            circle.centerX(),
            circle.centerY(),
            cr * progress,
            circlePaint
        )

        // Map our progress range from 0-1 to 0-PI
        val range = lerp(
            0F,
            Math.PI.toFloat(),
            progress
        )
        // Take the sin of our ranged progress * our maxScaleAddition as what we should
        // increase the icon's scale by.
        val additive = (sin(range.toDouble()) * iconMaxScaleAddition).coerceIn(0.0, 1.0)
        val scaleFactor = 1 + additive
        icon.setBounds(
            (circle.centerX() - (iconIntrinsicWidth / 2F) * scaleFactor).toInt(),
            (circle.centerY() - (iconIntrinsicHeight / 2F) * scaleFactor).toInt(),
            (circle.centerX() + (iconIntrinsicWidth / 2F) * scaleFactor).toInt(),
            (circle.centerY() + (iconIntrinsicHeight / 2F) * scaleFactor).toInt()
        )

        // Draw/animate the color of the icon
        icon.setTint(
            lerpArgb(iconTint, iconTintActive, 0F, 0.15F, progress)
        )

        // Draw the icon
        icon.draw(canvas)
    }
}