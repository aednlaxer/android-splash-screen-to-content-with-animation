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

class SplashView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val logoWidthHeight = resources.getDimensionPixelSize(R.dimen.splash_logo_width_height)
    private val viewportWidthHeight =
        resources.getInteger(R.integer.splash_logo_viewport_width_height)

    private val backgroundColor = context.getColor(R.color.colorPrimary)

    private val iconPaint: Paint = Paint().apply {
        isAntiAlias = true
        color = context.getColor(R.color.colorAccent)
        style = Paint.Style.FILL
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        alpha = 0
    }

    private val iconPath: Path = Path()
    private val iconMatrix: Matrix = Matrix()

    private var scale = 1f

    private var logoSideHalf: Int = 0
    private var scaledLogoWidthHeight: Int = 0  // FIXME float

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
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
        val scale = widthHeight / viewportWidthHeight.toFloat()
        canvas.save()
        canvas.translate(
            (widthHeight - scale * viewportWidthHeight) / 2f + dx,
            (widthHeight - scale * viewportWidthHeight) / 2f + dy
        )
        iconMatrix.reset()
        iconMatrix.setScale(scale, scale)
        canvas.save()

        canvas.scale(1.0f, 1.0f)
        canvas.save()
        iconPath.reset()
        iconPath.moveTo(0.0f, 0.0f)
        iconPath.lineTo(127.0f, 0.0f)
        iconPath.quadTo(127.0f, 0.0f, 127.0f, 0.0f)
        iconPath.lineTo(127.0f, 127.0f)
        iconPath.quadTo(127.0f, 127.0f, 127.0f, 127.0f)
        iconPath.lineTo(0.0f, 127.0f)
        iconPath.quadTo(0.0f, 127.0f, 0.0f, 127.0f)
        iconPath.lineTo(0.0f, 0.0f)
        iconPath.quadTo(0.0f, 0.0f, 0.0f, 0.0f)
        iconPath.transform(iconMatrix)
        canvas.drawPath(iconPath, iconPaint)
        canvas.restore()

        canvas.save()
        iconPath.reset()
        iconPath.moveTo(63.0f, 109.68f)
        iconPath.cubicTo(62.42f, 109.68f, 61.83f, 109.44f, 61.37f, 108.98f)
        iconPath.lineTo(21.81f, 69.42f)
        iconPath.cubicTo(10.06f, 57.67f, 10.06f, 38.48f, 21.81f, 26.73f)
        iconPath.cubicTo(33.1f, 15.56f, 51.13f, 15.09f, 63.0f, 25.45f)
        iconPath.cubicTo(68.47f, 20.56f, 75.45f, 18.0f, 82.89f, 18.0f)
        iconPath.cubicTo(90.92f, 18.0f, 98.48f, 21.14f, 104.18f, 26.84f)
        iconPath.cubicTo(109.88f, 32.54f, 113.03f, 40.1f, 113.03f, 48.13f)
        iconPath.cubicTo(113.03f, 56.16f, 109.88f, 63.72f, 104.18f, 69.42f)
        iconPath.lineTo(64.63f, 108.98f)
        iconPath.cubicTo(64.16f, 109.44f, 63.58f, 109.68f, 63.0f, 109.68f)
        iconPath.transform(iconMatrix)
        canvas.drawPath(iconPath, iconPaint)
        canvas.restore()

        canvas.restore()
        canvas.restore()
    }
}
