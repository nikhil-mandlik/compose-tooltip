package com.nikhil.here.compose_tooltip.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nikhil.here.compose_tooltip.ui.composable.MyToolTip
import com.nikhil.here.compose_tooltip.ui.composable.TipPosition


@Composable
fun TooltipScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        var topTipPosition by remember {
            mutableStateOf(TipPosition.Top())
        }

        MyToolTip(
            modifier = Modifier
                .width(160.dp)
                .height(100.dp),
            arrowPosition = topTipPosition,
            arrowHeight = 30f,
            tipColor = Color.Black
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                Text(
                    text = "Text under tooltip",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        Slider(
            value = topTipPosition.marginPercent,
            onValueChange = {
                topTipPosition = TipPosition.Top(it)
            }
        )

        Spacer(modifier = Modifier.height(20.dp))


        var bottomTipPosition by remember {
            mutableStateOf(TipPosition.Bottom())
        }

        MyToolTip(
            modifier = Modifier
                .width(160.dp)
                .height(100.dp),
            arrowPosition = bottomTipPosition,
            arrowHeight = 30f,
            tipColor = Color.Black
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                Text(
                    text = "Text under tooltip",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        Slider(
            value = bottomTipPosition.marginPercent,
            onValueChange = {
                bottomTipPosition = TipPosition.Bottom(it)
            }
        )
    }
}


