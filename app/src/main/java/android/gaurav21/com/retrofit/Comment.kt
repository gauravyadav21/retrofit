package android.gaurav21.com.retrofit

import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("body")
    var text : String,
    var name : String,
    var postId : Int,
    var email : String,
    var id : Int) {
}