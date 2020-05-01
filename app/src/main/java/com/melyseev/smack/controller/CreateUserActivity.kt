package com.melyseev.smack.controller

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.security.ProviderInstaller
import com.melyseev.smack.R

import com.melyseev.smack.services.AuthService
import com.melyseev.smack.services.UserDataSevice
import com.melyseev.smack.utilities.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.activity_create_user.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlin.math.truncate
import kotlin.random.Random



class CreateUserActivity : AppCompatActivity() {

    var userAvatar= "profileDefault"
    var avatarColor= "[0.5, 0.5, 0.5, 1]"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)

        progressBarCreateUser.visibility= View.INVISIBLE
    }



    fun generateUserAvatarClicked(view: View){
        val color = Random.nextInt(2)
        val avatarNumber = Random.nextInt(28)

        when(color){
            0->  userAvatar = "light$avatarNumber"
            else-> {
                userAvatar = "dark$avatarNumber"
            }
        }
        val resourceId= resources.getIdentifier(userAvatar, "drawable", packageName)
        imageViewCreateUserAvatar.setImageResource( resourceId )
    }

    fun generateColorBtnClicked(view: View){

        val red = Random.nextInt(255)
        val green = Random.nextInt(255)
        val blue = Random.nextInt(255)
        imageViewCreateUserAvatar.setBackgroundColor( Color.rgb( red, green, blue))

        val savedR = red.toDouble()/255
        val savedG= green.toDouble()/255
        val saveB= blue.toDouble()/255

        avatarColor= "[$savedR, $savedG, $saveB, 1]"
        println(avatarColor)
    }

    fun createUserBtnClicked(view: View) {

        val name= txtCreateUserName.text.toString()
        val email= txtCreateUserEmail.text.toString()
        val password = txtCreateUserPassword.text.toString()
        showLoading(true)
        AuthService.registerUser(this, email , password) { successRegistry ->
            if (successRegistry) {
                AuthService.loginUser(this, email, password) { successLogin ->
                    if (successLogin) {
                        println("login user - ${AuthService.currentUserEmail}")
                        println("login token - ${AuthService.currentToken}")

                            AuthService.createUser(
                                this,
                                name,
                                email,
                                userAvatar,
                                avatarColor) { successCreateUser ->
                                    if (successCreateUser) {
                                        println("created user, name - ${UserDataSevice.name}")

                                        val userDataChange= Intent(BROADCAST_USER_DATA_CHANGE)
                                        LocalBroadcastManager.getInstance(this).sendBroadcast( userDataChange )
                                        showLoading(false)
                                        finish()
                                    }else
                                        showErrorToast()
                        }
                    } else
                        showErrorToast()
                }
            }else
                showErrorToast()
        }
    }

    fun showLoading(loading: Boolean){
        if(loading){
            progressBarCreateUser.visibility= View.VISIBLE
        }else{
            progressBarCreateUser.visibility= View.INVISIBLE
        }
        txtCreateUserName.isEnabled= !loading
        txtCreateUserEmail.isEnabled= !loading
        txtCreateUserPassword.isEnabled=  !loading
    }

    fun showErrorToast(){
        Toast.makeText(this,"Something went wrong", Toast.LENGTH_LONG).show()
        showLoading( false )
    }


   }

