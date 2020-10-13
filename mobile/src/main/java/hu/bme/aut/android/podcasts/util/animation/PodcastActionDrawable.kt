package hu.bme.aut.android.podcasts.util.animation

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.appcompat.content.res.AppCompatResources
import hu.bme.aut.android.podcasts.R
import hu.bme.aut.android.podcasts.util.extensions.themeColor
import hu.bme.aut.android.podcasts.util.extensions.themeInterpolator
import kotlin.math.abs

abstract class PodcastActionDrawable(context: Context) : Drawable() {

    protected open val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.themeColor(R.attr.colorSecondary)
        style = Paint.Style.FILL
    }

    // Rect to represent the circle used for the background/circular reveal animation.
    protected open val circle = RectF()
    protected open var cx = 0F
    protected open var cr = 0F

    protected open val icon: Drawable = AppCompatResources.getDrawable(
        context,
        R.drawable.ic_twotone_star_on_background
    )!!
    protected open val iconMargin = context.resources.getDimension(R.dimen.grid_4)
    protected open val iconIntrinsicWidth = icon.intrinsicWidth
    protected open val iconIntrinsicHeight = icon.intrinsicHeight

    @ColorInt
    protected open val iconTint = context.themeColor(R.attr.colorOnBackground)

    @ColorInt
    protected open val iconTintActive = context.themeColor(R.attr.colorOnSecondary)

    // Amount that we should 'overshoot' the icon's scale by when animating.
    protected open val iconMaxScaleAddition = 0.5F

    protected open var progress = 0F
        set(value) {
            val constrained = value.coerceIn(0F, 1F)
            if (constrained != field) {
                field = constrained
                callback?.invalidateDrawable(this)
            }
        }
    protected open var progressAnim: ValueAnimator? = null
    protected open val dur = 225
    protected open val persistentInterpolator =
        context.themeInterpolator(R.attr.motionInterpolatorPersistent)

    abstract fun update()

    override fun onBoundsChange(bounds: Rect?) {
        if (bounds == null) return
        update()
    }

    override fun isStateful(): Boolean = true

    override fun onStateChange(state: IntArray?): Boolean {
        val initialProgress = progress
        val newProgress = if (state?.contains(android.R.attr.state_activated) == true) {
            1F
        } else {
            0F
        }
        progressAnim?.cancel()
        progressAnim = ValueAnimator.ofFloat(initialProgress, newProgress).apply {
            addUpdateListener {
                progress = animatedValue as Float
            }
            interpolator = persistentInterpolator
            duration = (abs(newProgress - initialProgress) * dur).toLong()
        }
        progressAnim?.start()
        return newProgress == initialProgress
    }

    override fun draw(canvas: Canvas) {}

    override fun setAlpha(alpha: Int) {
        circlePaint.alpha = alpha
    }

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun setColorFilter(filter: ColorFilter?) {
        circlePaint.colorFilter = filter
    }
}