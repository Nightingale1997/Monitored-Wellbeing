package com.ciu196.android.heartbeat

import User
import android.app.Activity
import android.os.CountDownTimer
import android.view.TextureView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.ciu196.android.monitored_wellbeing.R
import com.ciu196.android.monitored_wellbeing.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList


internal class OutputAnalyzer //this.chartDrawer = new ChartDrawer(graphTextureView);
    (private val activity: Activity) {

    //private final ChartDrawer chartDrawer;

    private var store: MeasureStore? = null
    private val measurementInterval = 45
    private val measurementLength =
        15000 // ensure the number of data points is the power of two
    private val clipLength = 3500
    private var detectedValleys = 0
    private var ticksPassed = 0
    private val valleys =
        CopyOnWriteArrayList<Long>()
    private var timer: CountDownTimer? = null
    private fun detectValley(): Boolean {
        val valleyDetectionWindowSize = 13
        val subList =
            store!!.getLastStdValues(valleyDetectionWindowSize)
        return if (subList.size < valleyDetectionWindowSize) {
            false
        } else {
            val referenceValue =
                subList[Math.ceil(valleyDetectionWindowSize / 2.toDouble()).toInt()].measurement
            for (measurement in subList) {
                if (measurement.measurement < referenceValue) return false
            }

            // filter out consecutive measurements due to too high measurement rate
            subList[Math.ceil(valleyDetectionWindowSize / 2.toDouble())
                .toInt()].measurement != subList[Math.ceil(valleyDetectionWindowSize / 2.toDouble())
                .toInt() - 1].measurement
        }
    }

    fun measurePulse(textureView: TextureView, cameraService: CameraService) {

        var bpmCheck = false;

        // 20 times a second, get the amount of red on the picture.
        // detect local minimums, calculate pulse.
        store = MeasureStore()
        detectedValleys = 0
        timer = object : CountDownTimer(measurementLength.toLong(), measurementInterval.toLong()) {
            override fun onTick(millisUntilFinished: Long) {
                // skip the first measurements, which are broken by exposure metering
                if (clipLength > ++ticksPassed * measurementInterval) return
                val thread = Thread(Runnable {
                    val currentBitmap = textureView.bitmap
                    val pixelCount = textureView.width * textureView.height
                    var measurement = 0
                    val pixels = IntArray(pixelCount)
                    currentBitmap.getPixels(
                        pixels,
                        0,
                        textureView.width,
                        0,
                        0,
                        textureView.width,
                        textureView.height
                    )

                    // extract the red component
                    // https://developer.android.com/reference/android/graphics/Color.html#decoding
                    for (pixelIndex in 0 until pixelCount) {
                        measurement += pixels[pixelIndex] shr 16 and 0xff
                    }
                    // max int is 2^31 (2147483647) , so width and height can be at most 2^11,
                    // as 2^8 * 2^11 * 2^11 = 2^30, just below the limit
                    store!!.add(measurement)
                    if (detectValley()) {
                        detectedValleys = detectedValleys + 1
                        valleys.add(store!!.lastTimestamp.time)
                        // in 13 seconds (13000 milliseconds), I expect 15 valleys. that would be a pulse of 15 / 130000 * 60 * 1000 = 69
                        val bpm = 60f * (detectedValleys - 1) / Math.max(
                            1f,
                            (valleys[valleys.size - 1] - valleys[0]) / 1000f
                        )

                        activity.runOnUiThread(Runnable {
                            (activity.findViewById<View>(R.id.bpm) as TextView).text = bpm.toInt().toString()
                        })

                    }
                })
                thread.start()
            }

            override fun onFinish() {
                val stdValues =
                    store!!.stdValues
                val bpm = 60f * (detectedValleys - 1) / Math.max(
                    1f,
                    (valleys[valleys.size - 1] - valleys[0]) / 1000f
                )
                // clip the interval to the first till the last one - on this interval, there were detectedValleys - 1 periods
                var currentValue = String.format(
                    Locale.getDefault(),
                    activity.resources.getQuantityString(
                        R.plurals.measurement_output_template,
                        detectedValleys - 1
                    ), bpm,
                    detectedValleys - 1,
                    1f * (valleys[valleys.size - 1] - valleys[0]) / 1000f
                )

                (activity.findViewById<View>(R.id.bpm) as TextView).text = bpm.toInt().toString()
                val returnValueSb = StringBuilder()
                returnValueSb.append(currentValue)
                returnValueSb.append(activity.getString(R.string.row_separator))

                // look for "drops" of 0.15 - 0.75 in the value
                // a drop may take 2-3 ticks.
                // int dropCount = 0;
                // for (int stdValueIdx = 4; stdValueIdx < stdValues.size(); stdValueIdx++) {
                //     if (((stdValues.get(stdValueIdx - 2).measurement - stdValues.get(stdValueIdx).measurement) > dropHeight) &&
                //             !((stdValues.get(stdValueIdx - 3).measurement - stdValues.get(stdValueIdx - 1).measurement) > dropHeight) &&
                //            !((stdValues.get(stdValueIdx - 4).measurement - stdValues.get(stdValueIdx - 2).measurement) > dropHeight)
                //    ) {
                //        dropCount++;
                //    }
                // }

                // returnValueSb.append(activity.getString(R.string.detected_pulse));
                // returnValueSb.append(activity.getString(R.string.separator));
                // returnValueSb.append((float) dropCount / ((float) (measurementLength - clipLength) / 1000f / 60f));
                // returnValueSb.append(activity.getString(R.string.row_separator));

                /*returnValueSb.append(activity.getString(R.string.raw_values));
                returnValueSb.append(activity.getString(R.string.row_separator));


                for (int stdValueIdx = 0; stdValueIdx < stdValues.size(); stdValueIdx++) {
                    // stdValues.forEach((value) -> { // would require API level 24 instead of 21.
                    Measurement<Float> value = stdValues.get(stdValueIdx);
                    returnValueSb.append(value.timestamp.getTime());
                    returnValueSb.append(activity.getString(R.string.separator));
                    returnValueSb.append(value.measurement);
                    returnValueSb.append(activity.getString(R.string.row_separator));
                }

                // add detected valleys location
                for (long tick : valleys) {
                    returnValueSb.append(tick);
                    returnValueSb.append(activity.getString(R.string.row_separator));
                }

                 */



                (activity.findViewById<View>(R.id.bpm) as TextView).setText(
                    bpm.toInt().toString()
                )

                if (bpm < 80) {
                    measurePulse(textureView, cameraService)
                }
                else{
                    cameraService.stop()
                    val checkIcon = activity.findViewById<View>(R.id.heartrateCheck) as ImageView
                    checkIcon.setImageResource(R.drawable.checkpassed)

                    val location = activity.findViewById<View>(R.id.locationCheck) as ImageView
                    if(location.getDrawable().getConstantState() ==
                        activity.getResources().getDrawable(R.drawable.checkpassed).getConstantState()){
                        val submit = activity.findViewById<View>(R.id.submitButton) as Button
                        submit.setEnabled(true)
                        submit.text="Submit"
                    }

                }
            }
        }
        timer!!.start()
    }

    fun stop() {
        if (timer != null) {
            timer!!.cancel()
        }
    }

}