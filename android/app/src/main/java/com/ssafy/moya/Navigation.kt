package com.ssafy.moya

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ssafy.ar.ARSceneComposable
import com.ssafy.main.encycdetail.EncycDetailScreen
import com.ssafy.main.encyclopedia.EncycScreen
import com.ssafy.main.exploredetail.ExploreDetailScreen
import com.ssafy.main.explorelist.ExploreListScreen
import com.ssafy.main.explorestart.ExploreStartScreen
import com.ssafy.main.login.LoginScreen
import com.ssafy.main.parkdetail.ParkDetailScreen
import com.ssafy.main.parklist.ParkListScreen
import com.ssafy.moya.navigation.MainBottomNavigation
import com.ssafy.ui.screen.UserProfileEditScreen

@Composable
fun MainNavigation(
    navController: NavHostController = rememberNavController(),
    ttsHelper: TTSHelper,
    sttHelper: STTHelper
) {
    // TODO startDestination 추후에 loin화면으로 수정
    NavHost(navController = navController, startDestination = Home) {
        composable<Home> {
            MainBottomNavigation(
                onNavigateToParkList = {
                    navController.navigate(ParkList)
                },
                onNavigateToParkDetail = { id ->
                    navController.navigate(ParkDetail(itemId = id))
                },
            )
//            HomeScreen(
//                onNavigateToParkList = {
//                    navController.navigate(ParkList)
//                },
//                onNavigateToParkDetail = { id ->
//                    navController.navigate(ParkDetail(itemId = id))
//                },
//                onNavigateToEncyc = { id ->
//                    navController.navigate(EncycDetail(itemId = id))
//                }
//            )
        }
        composable<ExploreList> {
            ExploreListScreen(onExploreItemClick = { itemId ->
                navController.navigate(ExploreDetail(itemId))
            }, onPop = {
                navController.popBackStack()
            })
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
        composable<ParkDetail> {
            val parkDetail = it.toRoute<ParkDetail>()
            ParkDetailScreen(
                parkId = parkDetail.itemId,
                onNavigateToEncycDetail = { itemId ->
                    navController.navigate(EncycDetail(itemId))
                },
                onPop = {
                    navController.popBackStack()
                },
                onEnterExplore = {
                    navController.navigate(ExploreStart(parkId = parkDetail.itemId))
                },
            )
        }
        composable<Encyc> {
            EncycScreen(onNavigateToEncycDetail = { itemId ->
                navController.navigate(EncycDetail(itemId))
            }, onPop = {
                navController.popBackStack()
            })
        }
        // TODO 추후에 찾으러가기 버튼을 눌렀을 때 해당하는 동식물을 찾으러 가는 네비게이션 추가
        composable<EncycDetail> {
            EncycDetailScreen(onPop = {
                navController.popBackStack()
            }, onTTSClicked = { fullText ->
                ttsHelper.speak(fullText)
            }, onTTSShutDown = {
                ttsHelper.shutdown()
            })
        }
        composable<ExploreStart> {
            val exploreStart = it.toRoute<ExploreStart>()
            // TODO 추후에 카메라 화면으로 이동하는 네비게이션 추가
            ExploreStartScreen(
                parkId = exploreStart.parkId,
                onExitExplore = {
                    navController.navigate(Home) {
                        // TODO 추후에 탐험기록 화면으로 이동하도록 수정
                        popUpTo(Home) { inclusive = true }
                    }
                },
                onEnterEncyc = {
                    navController.navigate(Encyc)
                },
                onEnterAR = {
                    navController.navigate(ARCamera)
                }
            )
        }
        composable<ARCamera> {
            ARSceneComposable( onPermissionDenied = {} )
        }
        composable<Login> {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(Home)
                },
            )
        }
        composable<UserProfileEdit> {
            UserProfileEditScreen()
        }
    }
}
