package ru.showjet.ratingwidget.view.widgets

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import ru.showjet.ratingwidget.R

class Ratings
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        init(context, attrs, defStyleAttr)
    }

    private var ratingBackgroundColor: Int = 0xFF4A4A4A.toInt()

    private lateinit var ratingImdb: Rating
    private lateinit var ratingKp: Rating
    private lateinit var space: View

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.Ratings, defStyleAttr, 0)
            try {
                ratingBackgroundColor = ta.getColor(R.styleable.Ratings_ratingsBackgroundColor, ratingBackgroundColor)
            } finally {
                ta.recycle()
            }
        }

        val view = LayoutInflater.from(context).inflate(R.layout.widget_ratings, this, false)
        view.setBackgroundColor(ratingBackgroundColor)
        addView(view)

        ratingImdb = view.findViewById(R.id.rating_widget_imdb)
        ratingKp = view.findViewById(R.id.rating_widget_kp)
        space = view.findViewById(R.id.space_view)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width0 = View.getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val height0 = View.getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)

        if (width0 <= 0 || height0 <= 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }

        val left = paddingLeft
        val right = width0 - paddingRight
        val paddedWidth = right - left

        // autosize space between ratings
        val spaceScale = 3f
        val spaceHeight = paddedWidth.toFloat() / spaceScale
        space.updateLayoutParams {
            height = spaceHeight.toInt()
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun setRatingImdb(rating: Float, animated: Boolean) {
        ratingImdb.setRating(rating, animated)
    }

    fun setRatingKp(rating: Float, animated: Boolean) {
        ratingKp.setRating(rating, animated)
    }
}


class Rating
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private var rating: Float = 0.0f
    private var source: CharSequence? = null

    private var ratingBackgroundColor: Int = 0xFF4A4A4A.toInt()
    private var ratingProgressBackgroundColor: Int = Color.TRANSPARENT
    private var ratingProgressProgressColor: Int = Color.WHITE
    private var ratingTextColor: Int = Color.WHITE

    private lateinit var ratingProgress: RatingProgress
    private lateinit var ratingValue: TextView
    private lateinit var ratingSource: TextView

    init {
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.Rating, defStyleAttr, 0)
            try {
                ratingBackgroundColor = ta.getColor(R.styleable.Rating_ratingBackgroundColor, ratingBackgroundColor)
                ratingProgressBackgroundColor = ta.getColor(R.styleable.Rating_ratingProgressBackgroundColor, ratingProgressBackgroundColor)
                ratingProgressProgressColor = ta.getColor(R.styleable.Rating_ratingProgressProgressColor, ratingProgressProgressColor)
                ratingTextColor = ta.getColor(R.styleable.Rating_ratingTextColor, ratingTextColor)

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

        ratingValue.setTextColor(ratingTextColor)
        ratingSource.setTextColor(ratingTextColor)
        ratingProgress.ratingProgressBackgroundColor = ratingProgressBackgroundColor
        ratingProgress.ratingProgressProgressColor = ratingProgressProgressColor

        setRating(rating)
        setSource(source)
    }

    fun setRating(/*@FloatRange(from = 0.0, to = 10.0) */rating: Float, animated: Boolean = false) {
        this.rating = rating.coerceIn(0.0f, 10.0f)

        ratingProgress.setRating(this.rating, animated)

        ratingValue.text = this.rating.toString()

        invalidate()
    }

    fun setSource(source: CharSequence?) {
        this.source = source

        ratingSource.text = source

        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width0 = View.getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        // with 'widthMeasureSpec' as 'heightMeasureSpec' to make squared layout
        val height0 = View.getDefaultSize(suggestedMinimumHeight, widthMeasureSpec)

        if (width0 <= 0 || height0 <= 0) {
            // with 'widthMeasureSpec' as 'heightMeasureSpec' to make squared layout
            super.onMeasure(widthMeasureSpec, widthMeasureSpec)
            return
        }

        val left = paddingLeft
        val right = width0 - paddingRight
        val paddedWidth = right - left

        // autosize textView
        val textSizeScale = 3.1f
        val textSize = paddedWidth.toFloat() / textSizeScale

        ratingValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        ratingSource.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)

        // with 'widthMeasureSpec' as 'heightMeasureSpec' to make squared layout
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}

class RatingProgress
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    var ratingProgressBackgroundColor: Int = 0xFF4A4A4A.toInt()
        set(value) {
            field = value
            updateColors()
        }
    var ratingProgressProgressColor: Int = Color.WHITE
        set(value) {
            field = value
            updateColors()
        }
    private val backgroundStartAngle = 0f
    private val backgroundSweepAngle = 360f
    private val progressStartAngle = 270f
    private var progressSweepAngle = 270f

    private val viewFrameF = RectF()
    private val paintBackground = Paint()
    private val paintProgress = Paint()

    override fun onFinishInflate() {
        super.onFinishInflate()

        setWillNotDraw(false)
    }

    private fun updateColors() {
        paintBackground.apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.SQUARE
            color = ratingProgressBackgroundColor
        }
        paintProgress.apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.SQUARE
            color = ratingProgressProgressColor
        }
        invalidate()
    }

    private var progress: Float = 0.0f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width0 = View.getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        // with 'widthMeasureSpec' as 'heightMeasureSpec' to make squared layout
        val height0 = View.getDefaultSize(suggestedMinimumHeight, widthMeasureSpec)

        if (width0 <= 0 || height0 <= 0) {
            // with 'widthMeasureSpec' as 'heightMeasureSpec' to make squared layout
            super.onMeasure(widthMeasureSpec, widthMeasureSpec)
            return
        }

        // for autosizing
        val progressStrokeWidthScale = 22f
        val progressStrokeWidth = (width0 - paddingLeft.toFloat() - paddingRight.toFloat()) / progressStrokeWidthScale
        val halfOfProgressStrokeWidth = progressStrokeWidth / 2
        val quartOfProgressStrokeWidth = progressStrokeWidth / 4

        paintBackground.strokeWidth = progressStrokeWidth - quartOfProgressStrokeWidth
        paintProgress.strokeWidth = progressStrokeWidth - quartOfProgressStrokeWidth

        viewFrameF.left = paddingLeft.toFloat() + halfOfProgressStrokeWidth
        viewFrameF.top = paddingTop.toFloat() + halfOfProgressStrokeWidth
        viewFrameF.right = width0.toFloat() - paddingRight.toFloat() - halfOfProgressStrokeWidth
        viewFrameF.bottom = height0.toFloat() - paddingBottom.toFloat() - halfOfProgressStrokeWidth

        // with 'widthMeasureSpec' as 'heightMeasureSpec' to make squared layout
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawArc(viewFrameF, backgroundStartAngle, backgroundSweepAngle, false, paintBackground)
        canvas.drawArc(viewFrameF, progressStartAngle, progressSweepAngle, false, paintProgress)
    }

    private val shiftDuration = 500L
    private var shiftAnimation: ValueAnimator? = null

    private fun animatedChangeProgressSweep(from: Float, to: Float) {

        shiftAnimation?.cancel()

        if (from == to) {
            setNewSweep(to)
            return
        }

        val va = ValueAnimator.ofFloat(from, to)
        va.interpolator = DecelerateInterpolator()
        va.duration = shiftDuration
        va.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                shiftAnimation = null
            }

            override fun onAnimationCancel(animation: Animator) {
                shiftAnimation = null
            }

            override fun onAnimationStart(animation: Animator) {
                shiftAnimation = va
            }
        })
        va.addUpdateListener { animation ->
            setNewSweep(animation.animatedValue as Float)
        }
        va.start()
    }

    private fun setNewSweep(newSweep: Float) {
        progressSweepAngle = newSweep
        invalidate()
    }

    fun setRating(rating: Float, animated: Boolean) {
        val oldProgressSweepAngle = calcSweep(this.progress)
        this.progress = rating
        val newProgressSweepAngle = calcSweep(this.progress)

        if (animated) {
            animatedChangeProgressSweep(oldProgressSweepAngle, newProgressSweepAngle)
        } else {
            setNewSweep(newProgressSweepAngle)
        }
    }

    private fun calcSweep(from: Float): Float {
        return from * 10 * 360 / 100
    }
}