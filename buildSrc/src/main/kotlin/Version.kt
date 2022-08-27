object Version {
    const val jvmTarget = "1.8"
    const val kotlin = "1.5.31" // compose 1.0.5 require 1.5.31

    object Android {
        const val toolsBuildGradle = "7.1.1"
        const val compileSdk = 31
        const val minSdk = 23
        const val targetSdk = compileSdk
        const val lifecycle = "2.4.1"
        const val compose = "1.0.5" // require kotlin 1.5.31
//        const val compose = "1.1.0" // require kotlin 1.6.10
    }

    object Application {
        const val code = 1
        const val name = "0.0.$code"
    }
}
