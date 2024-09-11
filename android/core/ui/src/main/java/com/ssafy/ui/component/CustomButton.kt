package com.example.uiexample.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uiexample.R
import com.example.uiexample.ui.theme.OnPrimaryColor
import com.example.uiexample.ui.theme.PrimaryColor
import com.example.uiexample.ui.theme.SecondarySurfaceColor

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
        Text(text = text)
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
        Text(text = text, color = Color.White)
    }
}

@Composable
fun CustomButtonWithImage(
    text: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonColor: Color = Color(0xFF9C6644),
    imagePainter: Painter? = null
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
        modifier = modifier
            .height(40.dp)
            .width(150.dp),
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
                    painter = imagePainter,
                    contentDescription = "Button Image",
                    modifier = Modifier
                        .size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            if (text != null) {
                Text(
                    text = text,
                    fontSize = 14.sp,
                    color = SecondarySurfaceColor
                )
            }
        }
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
    val imagePainter = painterResource(id = R.drawable.ic_launcher_foreground)
    CustomButtonWithImage(
        text = "도감",
        onClick = {},
        imagePainter = imagePainter
    )
}