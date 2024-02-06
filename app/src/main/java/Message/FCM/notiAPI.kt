package Message.FCM

import Message.FCM.Repo.Companion.CONTENT_TYPE
import Message.FCM.Repo.Companion.SERVER_KEY
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface notiAPI{
    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(@Body notification: pushNotification): Response<ResponseBody>

}