package com.example.splashscreendemo

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
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
        color = context.getColor(R.color.colorAccent)
        style = Paint.Style.FILL
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
//        alpha = 0
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
            duration = 4000
            addUpdateListener {
                scale = it.animatedValue as Float
                requestLayout()
                postInvalidate()
            }
            start()
        }

        // Alpha animation
        ValueAnimator.ofInt(0, 255).apply {
            startDelay = 300
            duration = 700
            addUpdateListener {
                iconPaint.alpha = it.animatedValue as Int
//                postInvalidate()
            }
//            start()
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
