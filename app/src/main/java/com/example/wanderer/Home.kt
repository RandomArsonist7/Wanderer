package com.example.app1jetpackcompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wanderer.R


@Composable
fun Home() {


    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f / 12f),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Anastazja Sofińska",
                        style = MaterialTheme.typography.headlineLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))


                    Text(
                        text = "Wanderer",
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            }
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(3f / 12f),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {

                    Text(
                        text = "Just a basic hiking assistant.",
                        style = TextStyle(
                            fontSize = 52.sp,
                            fontWeight = MaterialTheme.typography.headlineMedium.fontWeight
                        ),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(5f / 12f),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {

                    Image(
                        painter = painterResource(R.drawable.view),
                        contentDescription = "Penguin Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }
        }
    }
}

