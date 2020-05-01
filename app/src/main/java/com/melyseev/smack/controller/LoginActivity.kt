package com.melyseev.smack.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.melyseev.smack.R
import com.melyseev.smack.services.AuthService
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }


    fun loginLoginBtnClicked(view: View){
        val email= txtLoginEmail.text.toString()
        val password = txtLoginPassword.text.toString()

        AuthService.loginUser(this, email, password){
            loginSuccess->if (loginSuccess){
            AuthService.findUserByEmail(this){
                findSuccess->if(findSuccess){
                finish()
            }
            }
        }
        }
    }

    fun loginCreateUserBtnClicked(view: View){
        val intentCreateUser= Intent(this, CreateUserActivity::class.java)
        startActivity( intentCreateUser )
        finish()
    }
}
