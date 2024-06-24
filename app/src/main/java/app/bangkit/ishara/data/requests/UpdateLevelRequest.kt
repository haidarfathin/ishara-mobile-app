package app.bangkit.ishara.data.requests

import com.google.gson.annotations.SerializedName

data class UpdateLevelRequest(
    @SerializedName("obtained_stars")
    val obtainedStars: Int
)