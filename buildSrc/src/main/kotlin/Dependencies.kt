/**
 * Contains the versions of the dependencies being used
 */
object Versions {
    const val compileSdkVersion = 28
    const val minSdkVersion = 21
    const val targetSdkVersion = 30
    const val versionCode = 1
    const val versionName = "1"
    const val gradleBuildTool = "7.1.0-alpha01"
    const val kotlinVersion = "1.4.32"
    const val designSupportLib = "1.1.0-beta01"
    const val swipeRefreshLayout = "1.1.0"
    const val constraintLayout = "2.0.4"
    const val viewPager2 = "1.0.0"
    const val junit = "4.12"
    const val extJunit = "1.1.1"
    const val testRunner = "1.1.0"
    const val espresso = "3.1.0"
    const val espressoIdling = "3.1.1"
    const val rxJava = "2.1.10"
    const val rxAndroid = "2.0.2"
    const val rxKotlin = "2.2.0"
    const val retrofitVersion = "2.3.0"
    const val okHttp3Version = "3.10.0"
    const val archComponents = "2.3.1"
    const val testRule = "1.1.0"
    const val supportAnnotation = "1.2.0"
    const val appIntro = "5.1.0"
    const val appCompat = "1.3.0"
    const val circleImageView = "3.0.1"
    const val fragmentKtx = "1.3.4"
    const val composeVersion = "1.0.0-beta07"
}

/**
 * Contains the dependencies being used by the project
 */
object Dependencies {
    const val gradle_build_tool = "com.android.tools.build:gradle:${Versions.gradleBuildTool}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val kotlin_gradle_plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinVersion}"
    const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlinVersion}"
    const val design = "com.google.android.material:material:${Versions.designSupportLib}"
    const val constraint_layout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val junit = "junit:junit:${Versions.junit}"
    const val ext_junit = "androidx.test.ext:junit:${Versions.extJunit}"
    const val test_runner = "androidx.test:runner:${Versions.testRunner}"
    const val test_rules = "androidx.test:rules:${Versions.testRule}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val espresso_idling_resources = "androidx.test.espresso:espresso-idling-resource:${Versions.espressoIdling}"
    const val espresso_intents = "androidx.test.espresso:espresso-intents:${Versions.espresso}"
    const val rx_java = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}"
    const val rx_android = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"
    const val rx_kotlin = "io.reactivex.rxjava2:rxkotlin:${Versions.rxKotlin}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}"
    const val retrofit_gson_converter = "com.squareup.retrofit2:converter-gson:${Versions.retrofitVersion}"
    const val retrofit_rxjava2_adapter = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofitVersion}"
    const val okhttp3_logging_interceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okHttp3Version}"
    const val lifecycle_viewmodel = "androidx.lifecycle:lifecycle-viewmodel:${Versions.archComponents}"
    const val support_annotation = "androidx.annotation:annotation:${Versions.supportAnnotation}"
    const val app_intro = "com.github.AppIntro:AppIntro:${Versions.appIntro}"
    const val circule_image_view = "de.hdodenhof:circleimageview:${Versions.circleImageView}"
    const val fragment_ktx = "androidx.fragment:fragment-ktx:${Versions.fragmentKtx}"
    const val viewPager2 = "androidx.viewpager2:viewpager2:${Versions.viewPager2}"
    const val swipe_refresh_layout = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefreshLayout}"

    object Compose {
        const val UI = "androidx.compose.ui:ui:${Versions.composeVersion}"
        // Tooling support (Previews, etc.)
        const val Tooling = "androidx.compose.ui:ui-tooling:${Versions.composeVersion}"
        // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
        const val Foundation = "androidx.compose.foundation:foundation:${Versions.composeVersion}"
        // Material Design
        const val Material = "androidx.compose.material:material:${Versions.composeVersion}"
        // Material design icons
        const val Icons = "androidx.compose.material:material-icons-core:${Versions.composeVersion}"
        const val IconsExtended = "androidx.compose.material:material-icons-extended:${Versions.composeVersion}"
        // Integration with observables
        const val LiveData = "androidx.compose.runtime:runtime-livedata:${Versions.composeVersion}"
        const val Activity = "androidx.activity:activity-compose:1.3.0-alpha08"

        // UI Tests
        const val Test = "androidx.compose.ui:ui-test-junit4:${Versions.composeVersion}"
    }
}
