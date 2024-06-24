package app.bangkit.ishara.data.responses.journey

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class QuestionResponse(

	@field:SerializedName("data")
	val data: List<QuestionItem?>? = null,

	@field:SerializedName("meta")
	val meta: QuestionMeta? = null
)

data class AnswersItem(

	@field:SerializedName("answer")
	val answer: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("is_correct")
	val isCorrect: Boolean? = null
)

data class QuestionItem(
	@SerializedName("id")
	val id: Int,

	@SerializedName("type")
	val type: String,

	@SerializedName("title")
	val title: String,

	@SerializedName("question")
	val question: String,

	@SerializedName("correct_answer")
	val correctAnswer: Any,

	@SerializedName("image")
	val image: Any? = null,

	@SerializedName("answers")
	val answers: List<AnswersItem> = emptyList()
) : Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
		parcel.readString(),
	)

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeInt(id)
		parcel.writeString(type)
		parcel.writeString(title)
		parcel.writeString(question)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<QuestionItem> {
		override fun createFromParcel(parcel: Parcel): QuestionItem {
			return QuestionItem(parcel)
		}

		override fun newArray(size: Int): Array<QuestionItem?> {
			return arrayOfNulls(size)
		}
	}
}


data class QuestionMeta(

	@field:SerializedName("pagination")
	val pagination: Pagination? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
