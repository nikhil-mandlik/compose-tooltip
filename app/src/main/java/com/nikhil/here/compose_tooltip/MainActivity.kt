package com.nikhil.here.compose_tooltip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.nikhil.here.compose_tooltip.ui.screens.TooltipScreen
import com.nikhil.here.compose_tooltip.ui.theme.ComposetooltipTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposetooltipTheme {
                TooltipScreen()
            }
        }
    }
}

