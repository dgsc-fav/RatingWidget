package ru.showjet.ratingwidget.view.widgets

import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import ru.showjet.ratingwidget.R

class Rating : FrameLayout {

    private var rating: Float = 0.0f
    private var source: CharSequence? = null


    private var progressBackground: Int = 0
    private var progressColor: Int = 0
    private var textColor: Int = 0

    private val progressKoef = 1000
    lateinit var ratingProgress: ProgressBar
    lateinit var ratingValue: TextView
    lateinit var ratingSource: TextView

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.Rating, defStyleAttr, 0)
            try {
                rating = ta.getFloat(R.styleable.Rating_ratingValue, rating)
                source = ta.getString(R.styleable.Rating_ratingSource)
            } finally {
                ta.recycle()
            }
        }

        val view = LayoutInflater.from(context).inflate(R.layout.widget_rating_single, this, false)
        addView(view)

        ratingProgress = view.findViewById(R.id.rating_progress)
        ratingValue = view.findViewById(R.id.rating_value)
        ratingSource = view.findViewById(R.id.rating_source)


        //ratingProgress.max = 10 * progressKoef
    }

    fun setRating(/*@FloatRange(from = 0.0, to = 10.0) */rating: Float) {
        this.rating = rating
        ratingProgress.progress = (rating * progressKoef).toInt()

        invalidate()
    }

    fun source(source: CharSequence?) {
        this.source = source
        invalidate()
    }

    private val viewFrame = Rect()
    private val center = Point()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width0 = View.getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        // with 'widthMeasureSpec' as 'heightMeasureSpec' to make squared layout
        val height0 = View.getDefaultSize(suggestedMinimumHeight, widthMeasureSpec)

        if (width0 <= 0 || height0 <= 0) {
            // with 'widthMeasureSpec' as 'heightMeasureSpec' to make squared layout
            super.onMeasure(widthMeasureSpec, widthMeasureSpec)
            return
        }

        viewFrame.left = paddingLeft
        viewFrame.top = paddingTop
        viewFrame.right = width0 - paddingRight
        viewFrame.bottom = height0 - paddingBottom

        center.x = (viewFrame.bottom - viewFrame.top) / 2
        center.y = (viewFrame.right - viewFrame.left) / 2

        val textSize = viewFrame.width().toFloat() / 4

        ratingValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        ratingSource.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)

        // with 'widthMeasureSpec' as 'heightMeasureSpec' to make squared layout
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    private var mPaddedWidth: Int = 0
    private var mPaddedHeight: Int = 0

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

//        val added = !(boundByFrame && currentStartOfDay != viewDateStartOfDay || !this.isPeriodsAdded)
//        if (!changed && added) {
//            return
//        }

        // Let's initialize a completely reasonable number of variables.
        val w = right - left
        val h = bottom - top
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom
        val paddedRight = w - paddingRight
        val paddedBottom = h - paddingBottom
        val paddedWidth = paddedRight - paddingLeft
        val paddedHeight = paddedBottom - paddingTop

//        if (added && (paddedWidth == mPaddedWidth || paddedHeight == mPaddedHeight)) {
//            return
//        }

        mPaddedWidth = paddedWidth
        mPaddedHeight = paddedHeight


//        if (!added) {
//            currentStartOfDay = viewDateStartOfDay
//
//            if (boundByFrame) {
//                periodsStartTime = currentStartOfDay
//            }
//
//            fillPeriodItems()
//        }

        super.onLayout(changed, l, t, r, b)
    }

}