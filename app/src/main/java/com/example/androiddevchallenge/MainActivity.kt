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
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.androiddevchallenge.ui.component.UpdateClock
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.utils.Utility
import com.example.androiddevchallenge.utils.Utility.Companion.timerVal

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var time by rememberSaveable { mutableStateOf(timerVal) }
            Utility.initTimer(
                timerVal,
                object : Utility.TimerChangeListener {
                    override fun onTimeChange(timerVal: Long) {
                        time = timerVal
                    }
                }
            )
            MyTheme {
                Column {
                    UpdateClock(totalMillis = timerVal.toFloat(), currentMillis = time)
                }
            }
        }
    }
}
