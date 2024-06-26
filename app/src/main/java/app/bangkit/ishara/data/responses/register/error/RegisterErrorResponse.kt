package app.bangkit.ishara.data.responses.register.error

import app.bangkit.ishara.data.responses.login.error.Meta
import com.google.gson.annotations.SerializedName

data class RegisterErrorResponse(

	@field:SerializedName("meta")
	val meta: Meta
)

data class Meta(

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("error")
	val error: Error
)

data class Error(

	@field:SerializedName("email")
	val email: List<String>
)
