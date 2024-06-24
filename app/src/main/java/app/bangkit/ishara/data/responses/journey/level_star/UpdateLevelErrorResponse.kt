package app.bangkit.ishara.data.responses.journey.level_star

import com.google.gson.annotations.SerializedName

data class UpdateLevelErrorResponse(

	@field:SerializedName("meta")
	val meta: Meta? = null
)

data class ErrorMeta(

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("error")
	val error: String? = null
)
