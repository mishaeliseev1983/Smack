package com.melyseev.smack.controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.melyseev.smack.R
import com.melyseev.smack.services.AuthService
import com.melyseev.smack.services.UserDataSevice
import com.melyseev.smack.utilities.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.nav_header_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val userDatachangeReceiver= object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            if(AuthService.isLoggedIn){

                textViewEmailNavHeader.text=UserDataSevice.email
                textViewNameNavHeader.text=UserDataSevice.name
                buttonLoginNavHeader.text="LOGOUT"
                val resourceId= resources.getIdentifier(UserDataSevice.avatarName, "drawable", packageName)
                viewimageUserNavHeader.setImageResource( resourceId )

                val colorRgb=UserDataSevice.getRgbAvatarColor()
                viewimageUserNavHeader.setBackgroundColor(colorRgb)
            }
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.


        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)

        UserDataSevice.logout()
        textViewEmailNavHeader.text=""
        textViewNameNavHeader.text=""
        buttonLoginNavHeader.text="LOGIN"
        val resourceId= resources.getIdentifier("profiledefault", "drawable", packageName)
        viewimageUserNavHeader.setImageResource( resourceId )
        viewimageUserNavHeader.setBackgroundColor(Color.TRANSPARENT)

        val intentFilter= IntentFilter(BROADCAST_USER_DATA_CHANGE)
        LocalBroadcastManager.getInstance(this).registerReceiver( userDatachangeReceiver, intentFilter )
    }



    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun loginBtnNavClicked(view:View){
        if (AuthService.isLoggedIn){

            //logout
            UserDataSevice.logout()
            textViewEmailNavHeader.text=""
            textViewNameNavHeader.text=""
            buttonLoginNavHeader.text="LOGIN"
            val resourceId= resources.getIdentifier("profiledefault", "drawable", packageName)
            viewimageUserNavHeader.setImageResource( resourceId )
            viewimageUserNavHeader.setBackgroundColor(Color.TRANSPARENT)

        }else{
            //login
            val loginIntent= Intent(this, LoginActivity::class.java)
            startActivity( loginIntent )
        }

    }

    fun addChannelClicked(view: View){

    }

    fun sendMessageBtnClicked(view: View){

    }
}
