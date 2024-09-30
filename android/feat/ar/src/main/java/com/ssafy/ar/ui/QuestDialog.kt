package com.ssafy.ar.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ssafy.ar.data.QuestInfo
import com.ssafy.ar.data.QuestState
import com.ssafy.ar.data.ScriptInfo
import com.ssafy.ar.dummy.scripts

@Composable
fun QuestDialog(
    info: QuestInfo,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
        )
        {
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .wrapContentHeight()
                    .background(
                        color = Color.White,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "${info.npcName}의 부탁",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    text = buildAnnotatedString {
                        append(
                            if (info.isComplete == QuestState.WAIT) scripts[info.questType]?.description
                            else scripts[info.questType]?.checkDescription
                        )
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = Color.Red
                            )
                        ) {
                            append(info.speciesName)
                        }
                        append(
                            if (info.isComplete == QuestState.WAIT) " 1개를 모아줄 수 있어?"
                            else " 1개를 벌써 모아온 거야?"
                            
                        )
                    },
                )

                Spacer(modifier = Modifier.height(40.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(color = Color.LightGray)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ) {
                    Button(
                        onClick = { onDismiss() },
                        shape = RectangleShape,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black,
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.White,
                        ),

                        ) {
                        Text(
                            text =
                            if (info.isComplete == QuestState.WAIT) "거절"
                            else "취소",
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                            .background(color = Color.LightGray)
                    )

                    Button(
                        onClick = { onConfirm() },
                        shape = RectangleShape,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF1E90FF),
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.White,
                        ),
                    ) {
                        Text(
                            text =
                            if (info.isComplete == QuestState.WAIT) "수락"
                            else "확인",
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}

