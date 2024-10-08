package com.ssafy.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssafy.ui.R
import com.ssafy.ui.theme.DarkGrayColor

@Composable
fun UserProfileEditScreen() {
    var name by remember { mutableStateOf("") }

    UserProfileEditContent(
        name = name,
        onNameChange = { newName -> name = newName },
    )
}

@Composable
fun UserProfileEditContent(
    name: String,
    onNameChange: (String) -> Unit,
) {
    Scaffold { paddingValues ->
        Column(
            modifier =
                Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "profile",
                modifier =
                    Modifier
                        .clip(CircleShape)
                        .background(DarkGrayColor),
            )

            Spacer(modifier = Modifier.height(28.dp))

            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text("이름") },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserProfileEditScreenPreview() {
    UserProfileEditScreen()
}
