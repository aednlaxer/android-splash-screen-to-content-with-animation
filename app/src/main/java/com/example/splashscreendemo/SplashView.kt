package com.example.splashscreendemo

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
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
    }

    private val iconPath: Path = Path()
    private val iconMatrix: Matrix = Matrix()

    private var scale = 1f

    private var logoSideHalf: Int = 0
    private var scaledLogoWidthHeight: Int = 0  // FIXME float

    fun animateLogo() {
        // Logo scale - from 1 to huge
        ValueAnimator.ofFloat(1f, 1000f).apply {
            duration = 3000
            addUpdateListener {
                scale = it.animatedValue as Float
                requestLayout()
                postInvalidate()
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
        iconPath.moveTo(14.52f, 0.0f)
        iconPath.cubicTo(9.21f, 0.0f, 4.91f, 4.3f, 4.91f, 9.6f)
        iconPath.cubicTo(4.91f, 14.9f, 9.21f, 19.2f, 14.52f, 19.2f)
        iconPath.cubicTo(48.35f, 19.2f, 75.78f, 46.6f, 75.78f, 80.4f)
        iconPath.cubicTo(75.78f, 85.7f, 80.08f, 90.0f, 85.39f, 90.0f)
        iconPath.cubicTo(90.7f, 90.0f, 95.0f, 85.7f, 95.0f, 80.4f)
        iconPath.cubicTo(95.1f, 36.1f, 58.96f, 0.0f, 14.52f, 0.0f)
        iconPath.transform(iconMatrix)
        canvas.drawPath(iconPath, iconPaint)
        canvas.restore()

        canvas.save()
        iconPath.reset()
        iconPath.moveTo(14.52f, 33.0f)
        iconPath.cubicTo(9.21f, 33.0f, 4.91f, 37.3f, 4.91f, 42.6f)
        iconPath.cubicTo(4.91f, 47.9f, 9.21f, 52.2f, 14.52f, 52.2f)
        iconPath.cubicTo(30.13f, 52.2f, 42.84f, 64.8f, 42.84f, 80.4f)
        iconPath.cubicTo(42.84f, 85.7f, 47.15f, 90.0f, 52.45f, 90.0f)
        iconPath.cubicTo(57.76f, 90.0f, 62.07f, 85.7f, 62.07f, 80.4f)
        iconPath.cubicTo(62.07f, 54.3f, 40.74f, 33.0f, 14.52f, 33.0f)
        iconPath.transform(iconMatrix)
        canvas.drawPath(iconPath, iconPaint)
        canvas.restore()

        canvas.save()
        iconPath.reset()
        iconPath.moveTo(14.52f, 66.0f)
        iconPath.cubicTo(6.51f, 66.0f, 0.0f, 72.5f, 0.0f, 80.4f)
        iconPath.cubicTo(0.0f, 88.5f, 6.51f, 95.0f, 14.52f, 95.0f)
        iconPath.cubicTo(22.52f, 95.0f, 29.13f, 88.5f, 29.13f, 80.4f)
        iconPath.cubicTo(29.03f, 72.5f, 22.52f, 66.0f, 14.52f, 66.0f)
        iconPath.transform(iconMatrix)
        canvas.drawPath(iconPath, iconPaint)
        canvas.restore()

        canvas.restore()
        canvas.restore()
    }
}
