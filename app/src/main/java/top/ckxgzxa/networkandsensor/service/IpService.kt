package top.ckxgzxa.networkandsensor.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import top.ckxgzxa.networkandsensor.entity.ip.IpResult

interface IpService {

    /*https://www.mxnzp.com/api/ip/self?app_id=&app_secret=*/

    @GET("/api/ip/self")
    fun getRealAddress(
        @Query("app_id") app_id: String, @Query("app_secret") app_secret: String): Call<IpResult>

}