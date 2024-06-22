package app.bangkit.ishara.ui.custom_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import app.bangkit.ishara.R

class CustomTextButton : View {

    private val backgroundPaint = Paint()
    private val mBounds = Rect()
    private val pilganButtonPaint = Paint(Paint.FAKE_BOLD_TEXT_FLAG)
    var options: ArrayList<TxtButton> = arrayListOf()
        set(value) {
            field = value
            requestLayout()
        }
    var txtButton: TxtButton? = null

    private val cornerRadius = 30f  // Radius untuk rounded corner

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
            if (i % 2 == 0) {
                options[i].apply {
                    x = halfOfWidth - 300F
                    y = halfOfHeight + value
                }
            } else {
                options[i].apply {
                    x = halfOfWidth + 100F
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

    private fun drawButton(canvas: Canvas?, txtButton: TxtButton) {
        if (txtButton.isClicked) {
            backgroundPaint.color = ResourcesCompat.getColor(resources, R.color.darkBlue, null)
            pilganButtonPaint.color = ResourcesCompat.getColor(resources, R.color.white, null)
        } else {
            backgroundPaint.color = ResourcesCompat.getColor(resources, R.color.lightOrange, null)
            pilganButtonPaint.color = ResourcesCompat.getColor(resources, R.color.orange, null)
        }

        canvas?.save()

        canvas?.translate(txtButton.x as Float, txtButton.y as Float)
        val backgroundPath = Path()
        backgroundPath.addRoundRect(
            0F, 0F, 245F, 240F,
            cornerRadius, cornerRadius,
            Path.Direction.CCW
        )
        canvas?.drawPath(backgroundPath, backgroundPaint)

        pilganButtonPaint.textSize = 100F
        val text = txtButton.name
        pilganButtonPaint.getTextBounds(text, 0, text.length, mBounds)
        val textWidth = mBounds.width()
        val textHeight = mBounds.height()
        val xPos = (245F / 2) - (textWidth / 2)
        val yPos = (240F / 2) + (textHeight / 2)
        canvas?.drawText(text, xPos, yPos, pilganButtonPaint)

        canvas?.restore()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            for (i in options.indices) {
                val txtButton = options[i]
                if (event.x >= txtButton.x!! && event.x <= txtButton.x!! + 245F &&
                    event.y >= txtButton.y!! && event.y <= txtButton.y!! + 240F) {
                    options(i)
                    return true
                }
            }
        }
        return false
    }

    private fun options(position: Int) {
        for (txtButton in options) {
            txtButton.isClicked = false
        }
        options[position].apply {
            txtButton = this
            isClicked = true
        }
        invalidate()
    }
}

data class TxtButton(
    val id: Int,
    var x: Float? = 0f,
    var y: Float? = 0f,
    var name: String,
    var isClicked: Boolean,
)
