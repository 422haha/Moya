package com.ssafy.moya.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ssafy.main.encyclopedia.EncycScreen
import com.ssafy.main.explorelist.ExploreListScreen
import com.ssafy.main.home.HomeScreen
import com.ssafy.moya.R
import com.ssafy.ui.navigationbar.BottomNavigationBar
import com.ssafy.ui.navigationbar.BottomNavigationItemState

@Composable
fun MainBottomNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    onNavigateToParkList: () -> Unit = {},
    onNavigateToEncyc: (Long) -> Unit = {},
    onNavigateToJournal: () -> Unit = {},
    onNavigateToParkDetail: (Long) -> Unit = {},
    onNavigateToEncycDetail: (Long) -> Unit = {},
) {
    val bottomRoute by navController.currentBackStackEntryAsState()

    Scaffold(
        modifier = modifier,
        bottomBar = {
            BottomNavigationBar(
                startDestination = bottomRoute?.destination?.route,
                items =
                    listOf(
                        BottomNavigationItemState(
                            route = MainBottomNavigationRoute.Home,
                            label = "홈",
                            icon = R.drawable.home,
                            selectedIcon = R.drawable.home_filled,
                        ),
                        BottomNavigationItemState(
                            route = MainBottomNavigationRoute.Encyc,
                            label = "도감",
                            icon = R.drawable.encyclopedia,
                            selectedIcon = R.drawable.encyclopedia_filled,
                        ),
                        BottomNavigationItemState(
                            route = MainBottomNavigationRoute.ExploreJournal,
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
            startDestination = MainBottomNavigationRoute.Home,
        ) {
            composable<MainBottomNavigationRoute.Home> {
                HomeScreen(
                    onNavigateToParkList = {
                        onNavigateToParkList()
                    },
                    onNavigateToEncycDetail = { id ->
                        onNavigateToEncycDetail(id)
                    },
                    onNavigateToParkDetail = { id ->
                        onNavigateToParkDetail(id)
                    },
                )
            }
            composable<MainBottomNavigationRoute.Encyc> {
                EncycScreen(
                    isDialog = true,
                    parkId = 0,
                    onNavigateToEncycDetail = onNavigateToEncycDetail,
                    onPop = {},
                )
            }
            composable<MainBottomNavigationRoute.ExploreJournal> {
                ExploreListScreen(
                    page = 1,
                    size = 10,
                    onExploreItemClick = {},
                    onPop = {},
                )
            }
        }
    }
}
