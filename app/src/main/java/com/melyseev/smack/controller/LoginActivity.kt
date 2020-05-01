package com.melyseev.smack.controller

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.melyseev.smack.R
import com.melyseev.smack.services.AuthService
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        progressBarLogin.visibility = View.INVISIBLE
    }


    fun loginLoginBtnClicked(view: View){
        val email= txtLoginEmail.text.toString()
        val password = txtLoginPassword.text.toString()
        hideKeyBoard()
        showLoading(true)
        if(email.isNotEmpty() && password.isNotEmpty()) {
            AuthService.loginUser(this, email, password) { loginSuccess ->
                if (loginSuccess) {
                    AuthService.findUserByEmail(this) { findSuccess ->
                        if (findSuccess) {
                            showLoading(false)
                            finish()
                        } else {
                            //error find user
                            showErrorToast()
                        }
                    }
                } else {
                    //error login
                    showErrorToast()
                }
            }
        }else{
            showErrorToast("Please fill in both email and password")
        }
    }

    fun loginCreateUserBtnClicked(view: View){
        val intentCreateUser= Intent(this, CreateUserActivity::class.java)
        startActivity( intentCreateUser )
        finish()
    }


    fun showLoading(loading: Boolean){
        if(loading){
            progressBarLogin.visibility= View.VISIBLE
        }else{
            progressBarLogin.visibility= View.INVISIBLE
        }
        btnLoginCreateUser.isEnabled= !loading
        btnLoginLogin.isEnabled= !loading
    }

    fun showErrorToast(errorMessage:String="Something went wrong"){
        Toast.makeText(this,errorMessage, Toast.LENGTH_LONG).show()
        showLoading( false )
    }

    fun hideKeyBoard(){
        val inputManager= getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if(inputManager.isAcceptingText){
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }
}
