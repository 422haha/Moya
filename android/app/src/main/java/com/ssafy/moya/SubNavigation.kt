package com.ssafy.moya

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ssafy.main.encyclopedia.EncycScreen
import com.ssafy.main.explorelist.ExploreListScreen
import com.ssafy.main.home.HomeScreen
import com.ssafy.ui.theme.DarkGrayColor
import com.ssafy.ui.theme.PrimaryColor

@Composable
fun SubNavigation(
    modifier: Modifier = Modifier,
    parentNavController: NavController? = null,
    navController: NavHostController = rememberNavController(),
    onNavigateToParkList: () -> Unit = {},
    onNavigateToEncyc: () -> Unit = {},
    onNavigateToJournal: () -> Unit = {},
    onNavigateToParkDetail: (Long) -> Unit = {},
    onNavigateToEncycDetail: (Long) -> Unit = {},
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            BottomNavigationBar(
                startDestination = BottomNavigationRoute.Home,
                items =
                    listOf(
                        BottomNavigationItemState(
                            route = BottomNavigationRoute.Home,
                            label = "홈",
                            icon = R.drawable.home,
                            selectedIcon = R.drawable.home_filled,
                        ),
                        BottomNavigationItemState(
                            route = BottomNavigationRoute.Encyc,
                            label = "도감",
                            icon = R.drawable.encyclopedia,
                            selectedIcon = R.drawable.encyclopedia_filled,
                        ),
                        BottomNavigationItemState(
                            route = BottomNavigationRoute.ExploreJournal,
                            label = "탐험일지",
                            icon = R.drawable.journal,
                            selectedIcon = R.drawable.journal_filled,
                        ),
                    ),
                onSelected = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        },
    ) { paddingValues ->

        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            startDestination = BottomNavigationRoute.Home,
        ) {
            composable<BottomNavigationRoute.Home> {
                HomeScreen(
                    onNavigateToParkList = {
                        onNavigateToParkList()
                    },
                    onNavigateToEncyc = {},
                    onNavigateToParkDetail = { id ->
                        onNavigateToParkDetail(id)
                    },
                )
            }
            composable<BottomNavigationRoute.Encyc> {
                EncycScreen(
                    onNavigateToEncycDetail = {},
                    onPop = {},
                )
            }
            composable<BottomNavigationRoute.ExploreJournal> {
                ExploreListScreen(
                    onExploreItemClick = {},
                    onPop = {},
                )
            }
        }
    }
}

data class BottomNavigationItemState(
    val route: BottomNavigationRoute,
    val label: String,
    val icon: Int,
    val selectedIcon: Int,
)

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    items: List<BottomNavigationItemState>,
    onSelected: (route: BottomNavigationRoute) -> Unit,
    startDestination: BottomNavigationRoute,
) {
    var selectedRoute by remember { mutableStateOf(startDestination) }

    BottomAppBar {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            for (item in items) {
                BottomNavigationItem(
                    isSelected = item.route == selectedRoute,
                    label = item.label,
                    icon = item.icon,
                    selectedIcon = item.selectedIcon,
                    onClick = {
                        selectedRoute = item.route
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
        startDestination = BottomNavigationRoute.Home,
        items =
            listOf(
                BottomNavigationItemState(
                    route = BottomNavigationRoute.Home,
                    label = "홈",
                    icon = R.drawable.home,
                    selectedIcon = R.drawable.home_filled,
                ),
                BottomNavigationItemState(
                    route = BottomNavigationRoute.Encyc,
                    label = "도감",
                    icon = R.drawable.encyclopedia,
                    selectedIcon = R.drawable.encyclopedia_filled,
                ),
                BottomNavigationItemState(
                    route = BottomNavigationRoute.ExploreJournal,
                    label = "탐험일지",
                    icon = R.drawable.journal,
                    selectedIcon = R.drawable.journal_filled,
                ),
            ),
        onSelected = {},
    )
}
