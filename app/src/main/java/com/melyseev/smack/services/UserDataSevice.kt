package com.melyseev.smack.services

import android.graphics.Color
import java.util.*


object UserDataSevice {
    fun getRgbAvatarColor(): Int {
            val color= avatarColor.replace("]","")
                .replace("[","")
                .replace(",","")
            val scanner= Scanner(color)
            if(scanner.hasNext()){
                val r:Int = (scanner.nextDouble() * 255).toInt()
                val g:Int = (scanner.nextDouble() * 255).toInt()
                val b:Int = (scanner.nextDouble() * 255).toInt()
                return Color.rgb( r,g,b)
            }

        return 0
    }

    var avatarColor = ""
    var avatarName = ""
    var email:String = ""
    var name:String = ""
    var _id:String = ""


    fun logout(){
        avatarColor = ""
        avatarName = ""
        email = ""
        name = ""
        _id = ""
        AuthService.currentToken=""
        AuthService.isLoggedIn= false
    }
}