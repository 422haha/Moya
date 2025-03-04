package com.ssafy.moya

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ssafy.ar.ui.ARSceneComposable
import com.ssafy.main.encycdetail.EncycDetailScreen
import com.ssafy.main.encyclopedia.EncycScreen
import com.ssafy.main.exploredetail.ExploreDetailScreen
import com.ssafy.main.explorelist.ExploreListScreen
import com.ssafy.main.explorestart.ExploreStartScreen
import com.ssafy.main.home.HomeScreen
import com.ssafy.main.parkdetail.ParkDetailScreen
import com.ssafy.main.parklist.ParkListScreen
import com.ssafy.moya.login.LoginScreen
import com.ssafy.main.util.MultiplePermissionHandler
import com.ssafy.moya.navigation.MainBottomNavigation
import com.ssafy.ui.screen.UserProfileEditScreen

@Composable
fun MainNavigation(
    navController: NavHostController = rememberNavController(),
    ttsHelper: TTSHelper,
) {
    MultiplePermissionHandler(
        permissions =
            listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
    ) {}

    // TODO startDestination 추후에 loin화면으로 수정
    NavHost(navController = navController, startDestination = Login) {
        composable<Home> {
            MainBottomNavigation(
                onNavigateToParkList = {
                    navController.navigate(ParkList)
                },
                onNavigateToParkDetail = { id ->
                    navController.navigate(ParkDetail(parkId = id))
                },
                onNavigateToEncycDetail = { id ->
                    navController.navigate(EncycDetail(encycId = id))
                },
            )
        }
        composable<ExploreList> {
            ExploreListScreen(
                page = 1,
                size = 3,
                onExploreItemClick = { itemId ->
                    navController.navigate(ExploreDetail(itemId))
                },
                onPop = {
                    navController.popBackStack()
                },
            )
        }
        composable<ParkList> {
            ParkListScreen(onParkItemClick = { itemId ->
                navController.navigate(ParkDetail(itemId))
            }, onPop = {
                navController.popBackStack()
            })
        }
        composable<ExploreDetail> {
            ExploreDetailScreen(onEncycItemClicked = { itemId ->
                navController.navigate(EncycDetail(itemId))
            }, onPop = {
                navController.popBackStack()
            })
        }
        composable<ParkDetail> { navBackStackEntry ->
            val parkDetail = navBackStackEntry.toRoute<ParkDetail>()
            ParkDetailScreen(
                parkId = parkDetail.parkId,
                onNavigateToEncycDetail = { itemId ->
                    navController.navigate(EncycDetail(itemId))
                },
                onPop = {
                    navController.popBackStack()
                },
                onEnterExplore = {
                    navController.navigate(ExploreStart(parkId = parkDetail.parkId))
                },
            )
        }
        composable<Encyc> {
            val route = it.toRoute<Encyc>()
            EncycScreen(
                isDialog = route.isDialog,
                parkId = route.parkId,
                onNavigateToEncycDetail = { itemId ->
                    navController.navigate(EncycDetail(itemId))
                },
                onPop = {
                    navController.popBackStack()
                },
            )
        }

        composable<EncycDetail> {
            val item = it.toRoute<EncycDetail>()
            EncycDetailScreen(
                itemId = item.encycId,
                onPop = {
                    navController.popBackStack()
                },
                onTTSClicked = { fullText ->
                    ttsHelper.speak(fullText)
                },
                onTTSShutDown = {
                    ttsHelper.shutdown()
                },
                onTTSReStart = {
                    ttsHelper.reStart()
                },
            )
        }
        composable<ExploreStart> {
            val exploreStart = it.toRoute<ExploreStart>()
            ExploreStartScreen(
                parkId = exploreStart.parkId,
                onExitExplore = {
                    navController.navigate(Home) {
                        popUpTo(Home) { inclusive = true }
                    }
                },
                onEnterEncyc = { parkId ->
                    navController.navigate(Encyc(true, parkId))
                },
                onEnterAR = { id ->
                    navController.navigate(
                        ARCamera(
                            explrationId = id,
                            parkId = exploreStart.parkId,
                        ),
                    )
                },
            )
        }
        composable<ARCamera> { navBackStackEntry ->
            val route = navBackStackEntry.toRoute<ARCamera>()
            ARSceneComposable(
                explorationId = route.explrationId,
                onPop = {
                    navController.popBackStack()
                },
                onTTSClicked = { fullText ->
                    ttsHelper.speak(fullText)
                },
                onTTSShutDown = {
                    ttsHelper.shutdown()
                },
                onTTSReStart = {
                    ttsHelper.reStart()
                },
                onNavigateToEncyc = {
                    navController.navigate(Encyc(isDialog = true, parkId = route.parkId))
                },
            )
        }
        composable<Login> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Home)
                },
            )
        }
        composable<UserProfileEdit> {
            UserProfileEditScreen()
        }
    }
}
