package com.example.myapplication.retrofitTab

import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

interface PhoneService{

    @FormUrlEncoded
    @POST("/phone/upload")
    fun phoneUpload(
        @Field("name") name:String,     // input
        @Field("number") number:String
    ) : Call<PhoneInf>         // output, Login이라는 output

    @FormUrlEncoded
    @POST("/phone/delete")
    fun phoneDelete(
        @Field("name") name:String,     // input
        @Field("number") number:String
    ) : Call<PhoneInf>         // output, Login이라는 output

    @POST("/phone/download/all")
    fun phoneDownloadAll() : Call<Array<PhoneInf>>         // output, Login이라는 output

}

interface GalleryService {

    @FormUrlEncoded
    @POST("/image/upload")
    fun imageUpload(
        @Field("name") name:String,     // input
        @Field("file") file:String
    ) : Call<ImageInf>         // output, Login이라는 output

    @POST("/image/download/all")
    fun imageDownloadAll() : Call<Array<ImageInf>>         // output, Login이라는 output

}

/////////facebook
interface LoginService{
    @FormUrlEncoded
    @POST("login/facebook")
    fun postUser(
        @Field("fbUserData") fbUserData: JSONObject
    ):Call<LoginInf>
}
/////////facebook

interface ScheduleService {

    @FormUrlEncoded
    @POST("/schedule/upload")
    fun scheduleUpload(
        @Field("name") name:String,     // input
        @Field("file") file:String
    ) : Call<ScheduleInf>         // output, Login이라는 output

    @FormUrlEncoded
    @POST("/schedule/download/all")
    fun scheduleDownloadAll(
        @Field("name") name:String  // input
    ) : Call<Array<ScheduleInf>>         // output, Login이라는 output

}
