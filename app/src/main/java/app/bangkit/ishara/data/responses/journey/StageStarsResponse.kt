package app.bangkit.ishara.data.responses.journey

import com.google.gson.annotations.SerializedName

data class StageStarsResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("meta")
	val meta: StageStarMeta? = null
)

data class Data(

	@field:SerializedName("level")
	val level: Level? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("obtained_stars")
	val obtainedStars: Int? = null
)

data class Level(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class StageStarMeta(

	@field:SerializedName("pagination")
	val pagination: Pagination? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
