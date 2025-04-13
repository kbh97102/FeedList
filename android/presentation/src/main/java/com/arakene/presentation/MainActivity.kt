package com.arakene.presentation

import android.net.Uri
import android.os.Bundle
import android.util.Base64
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.arakene.domain.responses.VideoDto
import com.arakene.presentation.ui.FeedDetail
import com.arakene.presentation.ui.FeedListView
import com.arakene.presentation.ui.theme.FeedListTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder
import kotlin.reflect.KType
import kotlin.reflect.typeOf

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()

            FeedListTheme {

                NavHost(
                    navController = navController,
                    startDestination = Screens.Main,
                ) {

                    composable<Screens.Main> {
                        FeedListView(
                            navigate = {
                                navController.navigate(it)
                            }
                        )
                    }


                    composable<Screens.Detail>(
                        typeMap =
                            mapOf(
                                typeOf<VideoDto>() to VideoDtoNavType
                            )
                    ) {
                        val screenData = it.toRoute<Screens.Detail>()

                        FeedDetail(
                            videoDto = screenData.videoDto
                        )
                    }
                }

                CompositionLocalProvider(NavigationCompositionLocal provides { routeTarget ->
                    navController.navigate(routeTarget)
                }) {


                }
            }
        }
    }
}

/**
 * 데이터에 uri가 있다면 추가적인 인코딩을 해줘야 정상적인 동작을 함
 */
val VideoDtoNavType = object : NavType<VideoDto>(isNullableAllowed = false) {
    override fun put(bundle: Bundle, key: String, value: VideoDto) {
        bundle.putString(key, serializeAsValue(value))
    }

    override fun get(bundle: Bundle, key: String): VideoDto? {
        return bundle.getString(key)?.let { parseValue(it) }
    }

    override fun parseValue(value: String): VideoDto {
        val decodedJson = Uri.decode(value)
        return Json.decodeFromString(VideoDto.serializer(), decodedJson)
    }

    override fun serializeAsValue(value: VideoDto): String {
        val json = Json.encodeToString(VideoDto.serializer(), value)
        return Uri.encode(json)  // encode JSON to prevent special-character issues
    }

    override val name: String = "VideoDto"
    override fun equals(other: Any?): Boolean {
        // Ensure NavType equality so the nav system recognizes the type
        return other is NavType<*> && other.name == this.name && !other.isNullableAllowed
    }
}


inline fun <reified T> encodeToBase64(data: T): String {
    return URLEncoder.encode(Json.encodeToString(data))

    val json = Json.encodeToString(data)
    return Base64.encodeToString(json.toByteArray(), Base64.URL_SAFE or Base64.NO_WRAP)
}

inline fun <reified T> decodeFromBase64(encoded: String): T {
    return Json.decodeFromString(URLDecoder.decode(encoded))


    val json = String(Base64.decode(encoded, Base64.URL_SAFE or Base64.NO_WRAP))
    return Json.decodeFromString(json)
}


inline fun <reified T> customNavType(
    isNullableAllowed: Boolean = false,
) = object : NavType<T>(isNullableAllowed) {
    override fun get(bundle: Bundle, key: String): T? {
        return bundle.getString(key)?.let { Json.decodeFromString<T>(Uri.decode(it)) }
    }

    override fun parseValue(value: String): T {
        return Json.decodeFromString(Uri.decode(value))
    }

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putString(key, Uri.encode(Json.encodeToString(value)))
    }
}

inline fun <reified T> typeMap(): Map<KType, NavType<T>> {
    return mapOf(typeOf<T>() to customNavType<T>())
}


fun <T, U> Map<KType, NavType<T>>.plus(on: Map<KType, NavType<U>>): MutableMap<KType, NavType<*>> {
    return mutableMapOf<KType, NavType<*>>().apply {
        putAll(this@plus)
        putAll(on)
    }
}

val NavigationCompositionLocal = compositionLocalOf<(Screens) -> Unit> { {} }

sealed interface Screens {
    @Serializable
    data object Main : Screens

    @Serializable
    data class Detail(
        val videoDto: VideoDto
    ) : Screens
}