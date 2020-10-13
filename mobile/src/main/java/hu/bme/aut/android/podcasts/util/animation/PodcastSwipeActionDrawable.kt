package hu.bme.aut.android.podcasts.util.animation

import android.content.Context
import android.graphics.Canvas
import kotlin.math.hypot
import kotlin.math.sin

class PodcastSwipeActionDrawable(context: Context) : PodcastActionDrawable(context) {

    override fun update() {
        circle.set(
            bounds.left.toFloat(),
            bounds.top.toFloat(),
            bounds.right.toFloat(),
            bounds.bottom.toFloat()
        )

        val sideToIconCenter = iconMargin + (iconIntrinsicWidth / 2F)
        cx = circle.left + iconMargin + (iconIntrinsicWidth / 2F)
        // Get the longest visible distance at which the circle will be displayed (the hypotenuse of
        // the triangle from the center of the icon, to the furthest side of the rect, to the top
        // corner of the rect.
        cr = hypot(circle.right - sideToIconCenter, (circle.height() / 2F))

        callback?.invalidateDrawable(this)
    }

    override fun draw(canvas: Canvas) {
        // Draw the circular reveal background.
        canvas.drawCircle(
            cx,
            circle.centerY(),
            cr * progress,
            circlePaint
        )

        // Map our progress range from 0-1 to 0-PI
        val range = linearlyInterpolate(
            0F,
            Math.PI.toFloat(),
            progress
        )
        // Take the sin of our ranged progress * our maxScaleAddition as what we should
        // increase the icon's scale by.
        val additive = (sin(range.toDouble()) * iconMaxScaleAddition).coerceIn(0.0, 1.0)
        val scaleFactor = 1 + additive
        icon.setBounds(
            (cx - (iconIntrinsicWidth / 2F) * scaleFactor).toInt(),
            (circle.centerY() - (iconIntrinsicHeight / 2F) * scaleFactor).toInt(),
            (cx + (iconIntrinsicWidth / 2F) * scaleFactor).toInt(),
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