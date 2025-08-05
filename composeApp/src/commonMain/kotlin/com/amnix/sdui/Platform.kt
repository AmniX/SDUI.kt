package com.amnix.sdui

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform