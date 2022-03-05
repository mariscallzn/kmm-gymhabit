package com.andymariscal.gymhabit

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}