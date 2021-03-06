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
package com.example.androiddevchallenge.ui.component

import androidx.compose.animation.animateColor
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.ui.theme.ClockFont
import com.example.androiddevchallenge.ui.theme.dialFill
import com.example.androiddevchallenge.ui.theme.teal200
import com.example.androiddevchallenge.ui.theme.teal700
import com.example.androiddevchallenge.ui.theme.yellow700
import com.example.androiddevchallenge.utils.Utility
import java.util.concurrent.TimeUnit

@Composable
fun UpdateClock(totalMillis: Float, currentMillis: Long) {
    var perc by remember {
        mutableStateOf(360f)
    }

    if (currentMillis > 0) {
        perc = (360f - ((currentMillis / totalMillis) * 360f)) % 360f
    } else {
        perc = 360f
    }
    var transition = updateTransition(targetState = perc)
    Clock(transition, currentMillis)
}

@Composable
fun Clock(perc: Transition<Float>, currentMillis: Long) {

    val colorTransition = updateTransition(targetState = dialFill)
    val colorText = colorTransition.animateColor(transitionSpec = { tween(500) }) {
        if (perc.currentState < 360f) {
            yellow700
        } else {
            teal200
        }
    }
    val colorAnim = colorTransition.animateColor(
        transitionSpec = {
            when {
                perc.currentState < 90f ->
                    spring(stiffness = 80f)
                else -> {
                    spring(stiffness = 50f)
                }
            }
        }
    ) {
        dialFill.copy(alpha = perc.currentState / 360f)
    }

    ConstraintLayout(
        modifier = Modifier
            .padding(0.dp)
            .fillMaxHeight(1f)
            .background(color = Color(0xFF171B1E)),
    ) {
        val (clock, progressDial, count, buttons) = createRefs()
        val arc by perc.animateFloat(
            transitionSpec = {
                if (perc.currentState < 90) {
                    spring(stiffness = 20f)
                } else if (perc.currentState < 180f) {
                    spring(stiffness = 50f)
                } else if (perc.currentState < 270f) {
                    spring(stiffness = 80f)
                } else {
                    spring(stiffness = 100f)
                }
            }
        ) {
            if (currentMillis < 1000) {
                360F
            } else {
                it
            }
        }
        Canvas(
            modifier = Modifier
                .constrainAs(progressDial) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .wrapContentSize(Alignment.Center)
                .aspectRatio(1f)
                .padding(10.dp)
                .wrapContentSize(
                    Alignment.Center
                )
                .animateContentSize()
                .fillMaxSize(1f),
            onDraw = {
                val paint = android.graphics.Paint()
                val Color1 = teal700
                val Color2 = teal200
                val gradientShader: Shader = LinearGradientShader(
                    from = Offset(0f, 0f),
                    to = Offset(0f, 400f),
                    listOf(Color1, Color2)
                )
                paint.shader = gradientShader
                drawIntoCanvas {
                    it.nativeCanvas.drawArc(
                        35.dp.toPx(),
                        35.dp.toPx(),
                        size.width - 105f,
                        size.height - 105f,
                        270f,
                        arc,
                        true,
                        paint
                    )
                }
            }
        )

        Canvas(
            modifier = Modifier
                .constrainAs(clock) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .aspectRatio(1f)
                .padding(10.dp)
                .fillMaxHeight(1f)
                .wrapContentSize(
                    Alignment.Center
                )
                .background(color = Color.Yellow)
        ) {
            val middle = Offset(center.x, center.y)
            drawCircle(
                color = colorAnim.value,
                radius = 390f,
                style = Fill,
                center = middle
            )
        }

        Text(
            text =
            when (getTimer(currentMillis).equals("00:00")) {
                true -> "Time's Up!"
                false -> getTimer(currentMillis)
            },
            color = colorText.value,
            modifier = Modifier
                .constrainAs(count) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .fillMaxWidth(1f),
            textAlign = TextAlign.Center,
            fontFamily = ClockFont,
            fontSize = 40.sp
        )

        Box(
            modifier = Modifier
                .constrainAs(buttons) {
                    top.linkTo(clock.bottom, margin = 20.dp)
                    bottom.linkTo(parent.bottom)
                }
                .fillMaxWidth(1f)
        ) {
            LayMeOut()
        }
    }
}

@Composable
fun LayMeOut() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        LayButton(text = "Start")
//        LayButton(text = "Reset")
        LayButton(text = "Cancel")
    }
}

@Composable
fun LayButton(text: String) {
    Box(

        modifier = Modifier
            .wrapContentSize(Alignment.Center)
            .padding(10.dp)
            .clickable(enabled = true) {
                when (text) {
                    "Start" -> Utility.startTimer()
                    "Reset" -> Utility.resetTimer()
                    "Cancel" -> Utility.cancelTimer()
                }
            }
    ) {
        Image(
            modifier = Modifier
                .padding(10.dp)
                .size(40.dp, 40.dp),
            contentDescription = "Start the timer!",
            painter = painterResource(
                id = when (text) {
                    "Start" -> R.drawable.ic_play_button
                    "Reset" -> R.drawable.ic_reset
                    "Cancel" -> R.drawable.ic_cancel
                    else -> R.drawable.ic_play_button
                }
            ),
            colorFilter = ColorFilter.tint(color = teal200)
        )
    }
}

fun getTimer(millis: Long): String {
    val hms = String.format(
        "%02d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(
            TimeUnit.MILLISECONDS.toHours(
                millis
            )
        ),
        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(
            TimeUnit.MILLISECONDS.toMinutes(
                millis
            )
        )
    )
    return hms
}
