package ai.voya.testapp.model

import com.google.gson.annotations.SerializedName

data class Beer(
    val name: String,
    val firstBrewed: String?,
    @SerializedName("image_url") val imageUrl: String?,
    val description: String?
)