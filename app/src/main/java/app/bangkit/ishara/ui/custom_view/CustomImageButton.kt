package app.bangkit.ishara.ui.custom_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import app.bangkit.ishara.R

class CustomImageButton : View {

    private val backgroundPaint = Paint()
    var options: ArrayList<ImgButton> = arrayListOf()
        set(value) {
            field = value
            requestLayout()
        }
    var imgButton: ImgButton? = null

    private val cornerRadius = 40f

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val height = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)

        val halfOfHeight = height / 2
        val halfOfWidth = width / 2
        var value = -450F

        for (i in options.indices) {
            if (i.mod(2) == 0) {
                options[i].apply {
                    x = halfOfWidth - 400F
                    y = halfOfHeight + value
                }
            } else {
                options[i].apply {
                    x = halfOfWidth + 60F
                    y = halfOfHeight + value
                }
                value += 300F
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (option in options) {
            drawButton(canvas, option)
        }
    }

    private fun drawButton(canvas: Canvas?, imgButton: ImgButton) {
        if (imgButton.isClicked) {
            backgroundPaint.color = ResourcesCompat.getColor(resources, R.color.darkBlue, null)
        } else {
            backgroundPaint.color = ResourcesCompat.getColor(resources, R.color.lightOrange, null)
        }

        canvas?.save()

        canvas?.translate(imgButton.x as Float, imgButton.y as Float)
        val backgroundPath = Path()
//        backgroundPath.addRoundRect(
//            0F, 0F, 245F, 240F,
//            cornerRadius, cornerRadius,
//            Path.Direction.CCW
//        )
        canvas?.drawPath(backgroundPath, backgroundPaint)

        val drawableId =
            resources.getIdentifier(imgButton.imagePath, "drawable", context.packageName)
        val bitmap = BitmapFactory.decodeResource(resources, drawableId)
        bitmap?.let {
            val scaledBitmap = Bitmap.createScaledBitmap(it, 300, 300, false)
            val rect = RectF(0F, 0F, 350f, 350f)
            canvas?.drawRoundRect(rect, cornerRadius, cornerRadius, backgroundPaint)
            canvas?.drawBitmap(scaledBitmap, 25f, 20f, null)
        }

        canvas?.restore()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            for (i in options.indices) {
                val imgButton = options[i]
                when {
                    event.x >= imgButton.x!! && event.x <= imgButton.x!! + 400F &&
                            event.y >= imgButton.y!! && event.y <= imgButton.y!! + 225F -> {
                        options(i)
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun options(position: Int) {
        for (imgButton in options) {
            imgButton.isClicked = false
        }
        options[position].apply {
            imgButton = this
            isClicked = true
        }
        invalidate()
    }
}

data class ImgButton(
    val id: Int,
    var x: Float? = 0f,
    var y: Float? = 0f,
    var imagePath: String,
    var name: String,
    var isClicked: Boolean,
)
