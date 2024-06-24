package app.bangkit.ishara.data.responses.profile

import app.bangkit.ishara.data.responses.journey.Pagination
import com.google.gson.annotations.SerializedName

data class StarsResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("meta")
	val meta: Meta? = null
)

data class Meta(

	@field:SerializedName("pagination")
	val pagination: Pagination? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Data(

	@field:SerializedName("total_stars")
	val totalStars: Int? = null
)
