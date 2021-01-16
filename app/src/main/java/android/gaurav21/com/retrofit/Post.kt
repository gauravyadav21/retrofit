package android.gaurav21.com.retrofit

import com.google.gson.annotations.SerializedName

class Post{
    @SerializedName("body")
    var text : String?
    var title : String
    var userId : Int
    var id : Int? = null

    constructor(text1: String?, title1: String, userId1: Int){
        text = text1
        title = title1
        userId = userId1
    }

}