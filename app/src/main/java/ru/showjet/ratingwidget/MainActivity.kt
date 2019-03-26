package ru.showjet.ratingwidget

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import ru.showjet.ratingwidget.view.widgets.Ratings

class MainActivity : AppCompatActivity() {

    private lateinit var ratings: Ratings
    private lateinit var ratingsMedium: Ratings
    private lateinit var ratingsSmall: Ratings
    private lateinit var seekImdb: SeekBar
    private lateinit var seekKp: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        ratings = findViewById(R.id.ratings)
        ratingsMedium = findViewById(R.id.ratings_medium)
        ratingsSmall = findViewById(R.id.ratings_small)
        seekImdb = findViewById(R.id.seek_bar_imdb)
        seekKp = findViewById(R.id.seek_bar_kp)

        seekImdb.max = 100
        seekKp.max = 100

        seekImdb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                ratings.setRatingImdb(progress.toFloat() / 10, true)
                ratingsMedium.setRatingImdb(progress.toFloat() / 10, true)
                ratingsSmall.setRatingImdb(progress.toFloat() / 10, true)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })
        seekKp.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                ratings.setRatingKp(progress.toFloat() / 10, true)
                ratingsMedium.setRatingKp(progress.toFloat() / 10, true)
                ratingsSmall.setRatingKp(progress.toFloat() / 10, true)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })

        seekImdb.progress = 81
        seekKp.progress = 79
    }
}