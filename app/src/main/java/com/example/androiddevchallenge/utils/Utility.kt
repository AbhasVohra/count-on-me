/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.utils

import android.os.CountDownTimer

class Utility {

    companion object {
        val timerVal = 10000L
        lateinit var timer: CountDownTimer
        lateinit var listener: TimerChangeListener
        fun initTimer(time: Long, listener: TimerChangeListener) {
            this.listener = listener
            timer = object : CountDownTimer(time, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    listener.onTimeChange(millisUntilFinished)
                }

                override fun onFinish() {
                    listener.onTimeChange(0)
                }
            }
        }

        fun startTimer() {
            if (::timer.isInitialized) {
                timer.start()
                if (::listener.isInitialized) {
                    listener.onTimeChange(timerVal)
                }
            }
        }

        fun cancelTimer() {
            if (::timer.isInitialized) {
                timer.cancel()
                if (::listener.isInitialized) {
                    listener.onTimeChange(timerVal)
                }
            }
        }

        fun resetTimer() {
            if (::timer.isInitialized) {
                timer.cancel()
                if (::listener.isInitialized) {
                    listener.onTimeChange(timerVal)
                }
            }
        }
    }

    interface TimerChangeListener {
        fun onTimeChange(timerVal: Long)
    }
}
