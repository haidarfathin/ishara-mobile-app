package app.bangkit.ishara.data.retrofit

import app.bangkit.ishara.data.requests.LoginRequest
import app.bangkit.ishara.data.requests.RegisterRequest
import app.bangkit.ishara.data.responses.auth.RefreshTokenResponse
import app.bangkit.ishara.data.responses.journey.JourneyResponse
import app.bangkit.ishara.data.responses.journey.LevelResponse
import app.bangkit.ishara.data.responses.journey.QuestionResponse
import app.bangkit.ishara.data.responses.login.LoginResponse
import app.bangkit.ishara.data.responses.profile.ProfileResponse
import app.bangkit.ishara.data.responses.profile.StarsResponse
import app.bangkit.ishara.data.responses.register.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("api/v1/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @POST("api/v1/auth/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): RegisterResponse

    @POST("api/v1/auth/refresh-token")
    suspend fun refreshToken(
        @Header("Authorization") token: String
    ): RefreshTokenResponse

    @GET("api/v1/user/journey/stages")
    suspend fun getAllJourney(
        @Header("Authorization") token: String
    ): JourneyResponse

    @GET("api/v1/user/journey/stages/{stage_id}/levels")
    suspend fun getLevelonStages(
        @Header("Authorization") token: String,
        @Path("stage_id") stageId: Int,
    ): LevelResponse

    @GET("api/v1/user/journey/levels/{level_id}/questions")
    suspend fun getQuestionsLevel (
        @Header("Authorization") token: String,
        @Path("level_id") levelId: Int,
    ): QuestionResponse

    @GET("api/v1/user/profile")
    suspend fun getProfile(
        @Header("Authorization") token: String,
    ): ProfileResponse

    @GET("/api/v1/user/total-stars")
    suspend fun getUserTotalStar(
        @Header("Authorization") token: String,
    ): StarsResponse
}