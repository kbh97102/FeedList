package com.arakene.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun QualitySetting(
    list: List<String>
) {

    Column(
        modifier = Modifier
            .background(color = Color.Black.copy(alpha = 0.65f), shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 30.dp)
    ) {

        list.forEach {
            Text("TEXT $it", color = Color.White)
        }

    }
}


@Composable
@Preview(showBackground = true)
private fun QualitySettingPreview() {
    QualitySetting(
        buildList {
            repeat(10) {
                add("it $it")
            }
        }
    )
}