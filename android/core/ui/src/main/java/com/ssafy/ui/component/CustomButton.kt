package com.ssafy.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.ui.R
import com.ssafy.ui.theme.LightBackgroundColor
import com.ssafy.ui.theme.PrimaryColor
import com.ssafy.ui.theme.SecondaryColor
import com.ssafy.ui.theme.jua

@Composable
fun CustomOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, PrimaryColor),
        modifier = modifier,
        colors = ButtonColors(
            containerColor = Color.Transparent,
            contentColor = PrimaryColor,
            disabledContentColor = Color.White,
            disabledContainerColor = Color.Gray
        )
    ) {
        Text(text = text, fontFamily = jua)
    }
}

@Composable
fun CustomFilledButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonColor: Color = PrimaryColor
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
        modifier = modifier
    ) {
        Text(text = text, color = Color.White, fontFamily = jua)
    }
}

@Composable
fun CustomButtonWithImage(
    text: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonColor: Color = SecondaryColor,
    textColor: Color = LightBackgroundColor,
    imagePainter: Int? = null
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
        modifier = modifier
            .height(40.dp)
            .width(140.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (imagePainter != null) {
                Image(
                    painter = painterResource(id = imagePainter),
                    contentDescription = "Button Image",
                    modifier = Modifier
                        .size(32.dp)
                        .padding(4.dp)
                )
            }

            if (text != null) {
                Text(
                    text = text,
                    fontSize = 20.sp,
                    color = textColor,
                    fontFamily = jua,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}

@Composable
fun FindButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
    ) {
        Text(text = text, color = LightBackgroundColor, fontFamily = jua)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOutlinedButton() {
    CustomOutlinedButton(text = "아니오", onClick = {})
}

@Preview(showBackground = true)
@Composable
fun PreviewFilledButton() {
    CustomFilledButton(text = "네", onClick = {})
}

@Preview(showBackground = true)
@Composable
fun CustomButtonWithImagePreview() {
    CustomButtonWithImage(
        text = "도감",
        onClick = {},
        imagePainter = R.drawable.ic_launcher_foreground
    )
}