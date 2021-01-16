package android.gaurav21.com.retrofit

import retrofit2.Call
import retrofit2.http.*

interface JsonPlaceHolder {

    //@Query adds?query= to our host
    @GET("posts")
    fun getPosts(
        @Query("userId") userId: List<Int>,
        @Query("_sort") sort: String,
        @Query("_order") order: String) : Call<List<Post>>

    //Another way og doing @Query
    @GET("posts")
    fun getPosts(@QueryMap parameters: Map<String, String>): Call<List<Post>>

    //@Path will ill up {} and both should be same
    @GET("posts/{id}/comments")
    fun getComments(@Path("id") postId: Int) : Call<List<Comment>>

    @GET
    fun getComments(@Url url: String): Call<List<Comment>>

    @POST("posts")
    fun createPost(@Body post: Post): Call<Post>

    //Directly sending it through fields
    @FormUrlEncoded
    @POST("posts")
    fun createPost(
        @Field("userId") userId: Int,
        @Field("title") title: String,
        @Field("body") text: String) : Call<Post>

    @FormUrlEncoded
    @POST("posts")
    fun createPost(@FieldMap fields: Map<String, String>) : Call<Post>

    //with @PUT it replaces the existing data with the new data which we send
    @Headers("Static-Header1: 123", "Static-Header2: 456")
    @PUT("posts/{id}")
    fun putPost(
        @Header("Dynamic-Header") header: String,
        @Path("id") id: Int,
        @Body post: Post): Call<Post>

    //With @PATCH only the fields which we send is changed
    @PATCH("posts/{id}")
    fun patchPost(
        @HeaderMap headers: Map<String, String>,
        @Path("id") id: Int, @Body post: Post): Call<Post>

    @DELETE("posts/{id}")
    fun deletePost(@Path("id") id: Int): Call<Unit>
}