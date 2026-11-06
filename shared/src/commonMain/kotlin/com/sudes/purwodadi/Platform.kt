package com.sudes.purwodadi

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform