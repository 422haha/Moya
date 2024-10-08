package com.ssafy.ui.navigationbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.ui.R
import com.ssafy.ui.theme.DarkGrayColor
import com.ssafy.ui.theme.PrimaryColor


data class BottomNavigationItemState<T>(
    val route: T,
    val label: String,
    val icon: Int,
    val selectedIcon: Int,
)

@Composable
fun <T : Any> BottomNavigationBar(
    modifier: Modifier = Modifier,
    items: List<BottomNavigationItemState<T>>,
    onSelected: (route: T) -> Unit,
    startDestination: String?,
) {
    var selectedRoute by remember(startDestination) { mutableStateOf(startDestination) }

    BottomAppBar {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            for (item in items) {
                BottomNavigationItem(
                    isSelected = item.route::class.qualifiedName.toString() == selectedRoute,
                    label = item.label,
                    icon = item.icon,
                    selectedIcon = item.selectedIcon,
                    onClick = {
                        selectedRoute = item.route::class.qualifiedName.toString()
                        onSelected(item.route)
                    },
                )
            }
        }
    }
}

@Composable
fun BottomNavigationItem(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    label: String,
    icon: Int,
    selectedIcon: Int,
    onClick: () -> Unit = {},
) {
    Column(
        modifier =
        Modifier
            .width(80.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            tint = if (isSelected) PrimaryColor else DarkGrayColor,
            painter = painterResource(id = if (isSelected) selectedIcon else icon),
            contentDescription = "홈",
        )
        Text(
            text = label,
            color = if (isSelected) PrimaryColor else DarkGrayColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}


@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    BottomNavigationBar(
        startDestination = "",
        items =
        listOf(
            BottomNavigationItemState(
                route = "Home",
                label = "홈",
                icon = R.drawable.baseline_calendar_month_24,
                selectedIcon = R.drawable.baseline_calendar_month_24,
            ),
            BottomNavigationItemState(
                route = "Encyc",
                label = "도감",
                icon = R.drawable.baseline_calendar_month_24,
                selectedIcon = R.drawable.baseline_calendar_month_24,
            ),
            BottomNavigationItemState(
                route = "ExploreJournal",
                label = "탐험일지",
                icon = R.drawable.baseline_calendar_month_24,
                selectedIcon = R.drawable.baseline_calendar_month_24,
            ),
        ),
        onSelected = {},
    )
}
