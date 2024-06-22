package app.bangkit.ishara.ui.custom_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import app.bangkit.ishara.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class CustomImageButton : View {

    private val backgroundPaint = Paint()
    var options: ArrayList<ImgButton> = arrayListOf()
        set(value) {
            field = value
            requestLayout()
        }
    var imgButton: ImgButton? = null

    private val cornerRadius = 30f

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val height = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)

        if (options.size >= 2) {
            options[0].apply {
                x = 50f
                y = (height - 240) / 2f
            }
            options[1].apply {
                x = (width - 245 - 50).toFloat()
                y = (height - 240) / 2f
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
        backgroundPaint.color = if (imgButton.isClicked) {
            ResourcesCompat.getColor(resources, R.color.darkBlue, null)
        } else {
            ResourcesCompat.getColor(resources, R.color.lightOrange, null)
        }

        canvas?.save()

        canvas?.translate(imgButton.x as Float, imgButton.y as Float)
        val backgroundPath = Path().apply {
            addRoundRect(
                0f, 0f, 245f, 240f,
                cornerRadius, cornerRadius,
                Path.Direction.CCW
            )
        }
        canvas?.drawPath(backgroundPath, backgroundPaint)

        // Load image from URL using Glide
        Glide.with(this)
            .asBitmap()
            .load(imgButton.imagePath)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val scaledBitmap = Bitmap.createScaledBitmap(resource, 200, 200, false)
                    val rect = RectF(25f, 25f, 225f, 225f)
                    canvas?.drawRoundRect(rect, cornerRadius, cornerRadius, backgroundPaint)
                    canvas?.drawBitmap(scaledBitmap, 25f, 25f, null)
                    invalidate() // Trigger a redraw to display the image
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Handle placeholder if needed
                }
            })

        canvas?.restore()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            for (i in options.indices) {
                val imgButton = options[i]
                when {
                    event.x >= imgButton.x!! && event.x <= imgButton.x!! + 245f &&
                            event.y >= imgButton.y!! && event.y <= imgButton.y!! + 240f -> {
                        handleOptionClick(i)
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun handleOptionClick(position: Int) {
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
