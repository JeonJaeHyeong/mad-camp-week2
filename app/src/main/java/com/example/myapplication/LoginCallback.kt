package com.example.myapplication

import android.os.Bundle
import android.util.Log
import com.example.myapplication.retrofitTab.LoginInf
import com.example.myapplication.retrofitTab.LoginService
import com.facebook.*
import com.facebook.login.LoginResult
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginCallback : FacebookCallback<LoginResult> {
    val TAG = LoginCallback::class.java.simpleName
    override fun onSuccess(result: LoginResult?) {
        if (result?.accessToken != null) {
            val accessToken = result.accessToken
            Log.d(TAG, "get facebook")
            getFacebookInfo(accessToken)
        } else {
            Log.d(TAG, "access token is null")
        }
    }

    override fun onCancel() {
        Log.e("Callback :: ", "onCancel")
    }

    override fun onError(error: FacebookException?) {
        Log.e("Callback :: ", "onError : " + error?.message)
    }

    fun getFacebookInfo(accessToken: AccessToken) {
        val graphrequest: GraphRequest =
            GraphRequest.newMeRequest(accessToken, object : GraphRequest.GraphJSONObjectCallback {
                override fun onCompleted(resultObject: JSONObject?, response: GraphResponse?) {
                    try {
                        val name = resultObject?.getString("name")
                        val email = resultObject?.getString("email")
                        sendUserData(accessToken, name, email)
                        Log.d(TAG, "name $name + email $email")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Log.d(TAG, "ERROR")
                    }
                }
            })

        var parameters = Bundle()
        //parameters.putString("fields", "id,name,email,picture.width(200)")
        parameters.putString("fields", "id,name,email,gender,birthday")
        graphrequest.parameters = parameters
        graphrequest.executeAsync()
    }

    fun sendUserData(accessToken: AccessToken, name: String?, email: String?) {
        val fbAccessToken = accessToken.token

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.249.18.225:5000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val server: LoginService = retrofit.create(LoginService::class.java)
        val jsonObject = JSONObject()
        jsonObject.put("email", email)
        jsonObject.put("name", name)
        Log.d("START", "SENDING DATA")
        server.postUser(jsonObject).enqueue(object : Callback<LoginInf> {
            override fun onFailure(call: Call<LoginInf>?, t: Throwable?) {
                Log.e("retrofit fail", t.toString())
            }
            override fun onResponse(call: Call<LoginInf>?, response: Response<LoginInf>?) {
                Log.d("retrofit success", response.toString())
            }
        })

    }
}