package app.bangkit.ishara.data.responses.journey

import com.google.gson.annotations.SerializedName

data class LevelResponse(

	@field:SerializedName("data")
	val data: List<LevelItem?>? = null,

	@field:SerializedName("meta")
	val meta: LevelMeta? = null
)

data class LevelMeta(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class LevelItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
