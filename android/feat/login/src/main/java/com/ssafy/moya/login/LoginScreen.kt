package com.ssafy.moya.login

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.ssafy.moya.login.BuildConfig.OAUTH_CLIENT_ID
import com.ssafy.moya.login.BuildConfig.OAUTH_CLIENT_NAME
import com.ssafy.moya.login.BuildConfig.OAUTH_CLIENT_SECRET
import com.ssafy.ui.login.LoginScreenContent
import com.ssafy.ui.login.LoginUserIntent
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginScreenViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit = {},
) {
    val context = LocalContext.current
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        NaverIdLoginSDK.initialize(context, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME)
        viewModel.load()
    }

    fun login() {
        val oauthLoginCallback =
            object : OAuthLoginCallback {
                override fun onSuccess() {
                    coroutineScope.launch {
                        if (viewModel.login(
                                "naver",
                                NaverIdLoginSDK.getAccessToken().toString(),
                            )
                        ) {
                            onLoginSuccess()
                        } else {
                            Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(
                    httpStatus: Int,
                    message: String,
                ) {
                    val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                    val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                    Toast
                        .makeText(
                            context,
                            "errorCode:$errorCode, errorDesc:$errorDescription",
                            Toast.LENGTH_SHORT,
                        ).show()
                }

                override fun onError(
                    errorCode: Int,
                    message: String,
                ) {
                    onFailure(errorCode, message)
                }
            }

        NaverIdLoginSDK.authenticate(context, oauthLoginCallback)
    }

    LoginScreenContent(
        loginScreenState = uiState,
        onIntent = { intent ->
            when (intent) {
                is LoginUserIntent.OnLogin -> {
                    login()
                }
            }
        },
    )
}
