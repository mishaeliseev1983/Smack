package com.melyseev.smack.services

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.melyseev.smack.utilities.*
import org.json.JSONException
import org.json.JSONObject



object AuthService {

    var currentUserEmail = ""
    var currentToken=""
    var isLoggedIn= false

    /*
    fun registerUser(context: Context, email: String, password: String, complete: (Boolean)-> Unit){

        val jsonBody= JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)

        val requestBody = jsonBody.toString()
        val registerRequest= object :
            StringRequest(
                                    Method.POST,
                                    URL_REGISTER,
                                    Response.Listener {
                                        response -> println(response)
                                        complete(true)
                                    },
                                    Response.ErrorListener {
                                            error ->
                                            Log.d("ERROR", "Could not register user: $error")
                                            complete(false)
                                    }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        Volley.newRequestQueue(context).add( registerRequest )
    }

     */

    fun registerUser(context: Context, email: String, password: String, complete: (Boolean)-> Unit){

        println("AuthService.registerUser start params: email $email, password $password")

        val jsonBody= JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)

        val requestBody = jsonBody.toString()
        val registerRequest= object :
            StringRequest(
                Method.POST,
                URL_REGISTER,
                Response.Listener {

                   response -> println("AuthService.registerUser success response params: $response")
                   complete(true)
                },
                Response.ErrorListener {
                        error ->
                    Log.d("ERROR", "Could not register user: $error")
                    complete(false)
                }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        Volley.newRequestQueue(context).add( registerRequest )
    }

    fun loginUser(context: Context, email: String, password: String, complete: (Boolean)->Unit){
        val jsonBody= JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)

        println("AuthService.loginUser start params: email $email, password $password")
        val requestBody = jsonBody.toString()

        val loginRequest= object :
            JsonObjectRequest(
            Method.POST,
            URL_LOGIN,
            null,
            Response.Listener{

            try{
                currentUserEmail = it.getString("user")
                currentToken = it.getString("token")
                isLoggedIn = true
            }catch (ex: JSONException){
                Log.d("JSON", "EXC: "+ ex.localizedMessage)
                complete(false)
            }


            println("AuthService.loginUser success response params: user's email = $currentUserEmail, token = $currentToken" )
            complete(true)
            },Response.ErrorListener {
                jsonErrorResponse->
                    Log.d("ERROR", "Could not login user: $jsonErrorResponse")
                complete(false)
            })
            {
            override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }
            override fun getBody(): ByteArray {
                    return requestBody.toByteArray()
                }
            }

        Volley.newRequestQueue(context).add(loginRequest)
    }

    fun createUser(context: Context, name: String, email: String, avatarName: String, avatarColor: String, complete:(Boolean)->Unit){

        val jsonBody= JSONObject()
        jsonBody.put("name", name)
        jsonBody.put("email", email)
        jsonBody.put("avatarName", avatarName)
        jsonBody.put("avatarColor", avatarColor)


        println("UserDataSevice.createUser start params: name $name, email $email, avatarName $avatarName, avatarColor $avatarColor")
        val requestBody = jsonBody.toString()

        val createUserRequest= object :
            JsonObjectRequest(
                Method.POST,
                URL_CREATE_USER,
                null,
                Response.Listener{

                    try{
                        UserDataSevice.avatarColor= it.getString("avatarColor")
                        UserDataSevice.avatarName = it.getString("avatarName")
                        UserDataSevice.email= it.getString("email")
                        UserDataSevice.name= it.getString("name")
                        UserDataSevice._id= it.getString("_id")

                    }catch (ex: JSONException){
                        Log.d("JSON", "EXC: "+ ex.localizedMessage)
                        complete(false)
                    }


                    println("AuthService.createUser success response params: user name = ${UserDataSevice.name}" )
                    complete(true)
                }, Response.ErrorListener {
                        jsonErrorResponse->
                    Log.d("ERROR", "Could not create user: $jsonErrorResponse")
                    complete(false)
                })
        {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                var returnMap = HashMap<String, String>()
                returnMap.put("Authorization", "Bearer ${AuthService.currentToken}")
                return returnMap
            }
        }

        Volley.newRequestQueue(context).add(createUserRequest)
    }

    fun findUserByEmail(context: Context, complete: (Boolean) -> Unit){

        val findUser= object : JsonObjectRequest(Method.GET, "$URL_GET_USER$currentUserEmail",
            null,
            Response.Listener{ response->
                try{
                    UserDataSevice.avatarColor= response.getString("avatarColor")
                    UserDataSevice.avatarName = response.getString("avatarName")
                    UserDataSevice.email= response.getString("email")
                    UserDataSevice.name= response.getString("name")
                    UserDataSevice._id= response.getString("_id")

                    val userDataChange= Intent(BROADCAST_USER_DATA_CHANGE)
                    LocalBroadcastManager.getInstance(context).sendBroadcast(userDataChange)
                    complete(true)

                }catch (ex: JSONException){
                    Log.d("JSON", "EXC: "+ ex.localizedMessage)
                    complete(false)
                }

            },
            Response.ErrorListener{error -> Log.d("ERROR", "Could not find user")}
            ){
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                var returnMap = HashMap<String, String>()
                returnMap.put("Authorization", "Bearer ${AuthService.currentToken}")
                return returnMap
            }
        }
        Volley.newRequestQueue(context).add(findUser)

        /*val findUserRequest = object :JsonObjectRequest(
            Method.GET,
            "$URL_GET_USER$currentUserEmail",
            null,
            Response.Listener { response ->   },
            Response.ErrorListener { error ->  Log.d("ERROR", "Could not find user")},

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
                    
            override fun getHeaders(): MutableMap<String, String> {
                var returnMap = HashMap<String, String>()
                returnMap.put("Authorization", "Bearer ${AuthService.currentToken}")
                return returnMap
            }
        )

         */

    }


    fun logout(){

    }
}