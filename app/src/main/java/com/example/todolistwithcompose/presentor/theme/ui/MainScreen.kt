package com.example.todolistwithcompose.presentor.theme.ui

import androidx.compose.foundation.layout.*

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import java.time.LocalDate



@Composable
fun Task(title:String, content:String, time:String, groupLabel:String, date: LocalDate) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                fontFamily = FontFamily.SansSerif
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = groupLabel,
                    color = Color.Blue,
                    modifier = Modifier.weight(1f),
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = time,
                        modifier = Modifier.padding(end = 20.dp),
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = date.toString()
                    )
                }

            }
        }
    }

}


@Composable
@Preview
fun TaskPreview() {
    Task(
        title = "Note title",
        content = "Note content",
        time = "12:45",
        groupLabel = "Note group label",
        date = LocalDate.of(2024, 6, 18)
    )
}



@Composable
fun MainScreen(viewModel:ViewModel){
}