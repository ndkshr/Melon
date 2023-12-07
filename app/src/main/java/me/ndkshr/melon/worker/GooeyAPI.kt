package me.ndkshr.melon.worker

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface GooeyAPI {
    @POST("text2audio/")
    suspend fun gooeyFetchSong(
        @Body body: GooeySongRequest,
        @Header("Authorization") key: String,
        @Query("run_id") runId: String = RUN_ID,
        @Query("uid") uid: String = UID,
        @Header("Content-Type") mimeType: String = "application/json"
    ): Response<GooeySongResponse>
}