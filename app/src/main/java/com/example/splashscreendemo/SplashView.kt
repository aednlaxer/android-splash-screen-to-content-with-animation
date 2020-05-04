package com.example.splashscreendemo

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.PathParser

class SplashView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val logoWidthHeight: Float
    private val viewportWidthHeight: Float

    private val backgroundColor = context.getColor(R.color.colorPrimary)

    private val iconPaint: Paint = Paint().apply {
        isAntiAlias = true
        color = Color.WHITE
        style = Paint.Style.FILL
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        alpha = 255
    }

    private val originalIconPath: Path?
    private val iconPath: Path = Path()
    private val iconMatrix: Matrix = Matrix()

    private var scale = 1f

    private var logoSideHalf: Int = 0
    private var scaledLogoWidthHeight: Int = 0

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        val parsedVectorDrawable =
            VectorDrawableParser.parsedVectorDrawable(resources, R.drawable.ic_splash_logo)

        originalIconPath =
            parsedVectorDrawable?.pathData?.let { PathParser.createPathFromPathData(it) }

        // Assuming vector drawable is square
        logoWidthHeight = dp2px(parsedVectorDrawable?.width ?: 0f)
        viewportWidthHeight = parsedVectorDrawable?.viewportWidth ?: 0f
    }

    fun animateLogo() {
        // Logo scale - from 1 to huge
        ValueAnimator.ofFloat(1f, 100f).apply {
            duration = 2500

            // Update scale
            addUpdateListener {
                scale = it.animatedValue as Float
                requestLayout()
                postInvalidate()
            }

            // Completely hide view when animation ends
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    this@SplashView.visibility = GONE
                }

                override fun onAnimationCancel(animation: Animator?) {
                    this@SplashView.visibility = GONE
                }
            })

            start()
        }

        // Alpha animation
        ValueAnimator.ofInt(255, 1).apply {
            startDelay = 100
            duration = 1000
            addUpdateListener { iconPaint.alpha = it.animatedValue as Int }
            start()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        scaledLogoWidthHeight = (scale * logoWidthHeight).toInt()
        logoSideHalf = scaledLogoWidthHeight / 2
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // First, draw background color
        canvas.drawColor(backgroundColor)

        // Second, draw logo
        drawLogo(
            canvas,
            scaledLogoWidthHeight,
            width / 2 - logoSideHalf,
            height / 2 - logoSideHalf
        )
    }

    private fun drawLogo(canvas: Canvas, widthHeight: Int, dx: Int, dy: Int) {
        originalIconPath ?: return

        val scale = widthHeight / viewportWidthHeight
        canvas.save()
        canvas.translate(
            (widthHeight - scale * viewportWidthHeight) / 2f + dx,
            (widthHeight - scale * viewportWidthHeight) / 2f + dy
        )

        iconMatrix.reset()
        iconMatrix.setScale(scale, scale)
        canvas.scale(1.0f, 1.0f)
        iconPath.reset()
        iconPath.addPath(originalIconPath)
        iconPath.transform(iconMatrix)
        canvas.drawPath(iconPath, iconPaint)
        canvas.restore()
    }
}
