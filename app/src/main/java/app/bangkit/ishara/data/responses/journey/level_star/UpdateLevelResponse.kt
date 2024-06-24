package app.bangkit.ishara.data.responses.journey.level_star

import com.google.gson.annotations.SerializedName

data class UpdateLevelResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("meta")
	val meta: Meta? = null
)

data class Meta(

	@field:SerializedName("pagination")
	val pagination: Pagination? = null,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String? = null
)

data class Level(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class Pagination(
	val any: Any? = null
)

data class Data(

	@field:SerializedName("level")
	val level: Level? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("obtained_stars")
	val obtainedStars: Int? = null
)
