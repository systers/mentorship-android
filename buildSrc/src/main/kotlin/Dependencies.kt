/**
 * Contains the versions of the dependencies being used
 */
object Versions {
    const val compileSdkVersion = 28
    const val minSdkVersion = 19
    const val targetSdkVersion = 28
    const val versionCode = 1
    const val versionName = "1"
    const val gradleBuildTool = "4.2.1"
    const val kotlinVersion = "1.4.21"
    const val supportLib = "1.0.0"
    const val designSupportLib = "1.1.0-beta01"
    const val swipeRefreshLayout = "1.1.0"
    const val constraintLayout = "1.1.3"
    const val viewPager2 = "1.0.0"
    const val junit = "4.12"
    const val extJunit = "1.1.1"
    const val testRunner = "1.1.0"
    const val espresso = "3.1.0"
    const val espressoIdling = "3.1.1"
    const val retrofitVersion = "2.9.0"
    const val okHttp3Version = "3.10.0"
    const val archComponents = "2.0.0"
    const val testRule = "1.1.0"
    const val supportAnnotation = "1.0.0"
    const val appIntro = "5.1.0"
    const val appCompat = "1.0.2"
    const val circleImageView = "3.0.1"
    const val fragmentKtx = "1.2.5"
    const val navigationVersion = "2.3.5"
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
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}"
    const val retrofit_gson_converter = "com.squareup.retrofit2:converter-gson:${Versions.retrofitVersion}"
    const val okhttp3_logging_interceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okHttp3Version}"
    const val lifecycle_extensions = "androidx.lifecycle:lifecycle-extensions:${Versions.archComponents}"
    const val lifecycle_viewmodel = "androidx.lifecycle:lifecycle-viewmodel:${Versions.archComponents}"
    const val support_annotation = "androidx.annotation:annotation:${Versions.supportAnnotation}"
    const val app_intro = "com.github.AppIntro:AppIntro:${Versions.appIntro}"
    const val circule_image_view = "de.hdodenhof:circleimageview:${Versions.circleImageView}"
    const val fragment_ktx = "androidx.fragment:fragment-ktx:${Versions.fragmentKtx}"
    const val viewPager2 = "androidx.viewpager2:viewpager2:${Versions.viewPager2}"
    const val swipe_refresh_layout = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefreshLayout}"
    const val navigation_fragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigationVersion}"
    const val navigation_ui = "androidx.navigation:navigation-ui-ktx:${Versions.navigationVersion}"
    const val navigation_safe_args_plugin = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigationVersion}"
}
