package android.gaurav21.com.retrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    lateinit var textView: TextView
    lateinit var jsonPlaceHolder: JsonPlaceHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.text_view_result)

        // To put null values while using patch. Force Serialization of null Values
        var gson: Gson = GsonBuilder().serializeNulls().create()

        //For logging purpose
        var httpLoggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        //This interceptor header will be added to all methods in JsonPlaceHolder and no need to add them separately in each methods
        var okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(object: Interceptor {
                override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                    var originalRequest : Request = chain.request()
                    var newRequest = originalRequest.newBuilder()
                        .header("Interceptor-header","xyz")
                        .build()
                    return chain.proceed(newRequest)
                }

            })
            .addInterceptor(httpLoggingInterceptor)
            .build()

        var retrofit : Retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create(gson)) // to convert JSON to Java objects
            .client(okHttpClient)
            .build()

        jsonPlaceHolder = retrofit.create(JsonPlaceHolder::class.java)

        getPost()
//        getComments()
//        createPost()
//        updatePost()
//        deletePost()
    }

    private fun deletePost() {
        var call: Call<Unit> = jsonPlaceHolder.deletePost(5)

        call.enqueue(object: Callback<Unit>{
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                textView.setText(t.message)
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                textView.setText("Code: "+ response.code())
            }
        })
    }

    private fun updatePost() {
        var post: Post = Post(null, "War", 12)

        //var call: Call<Post> = jsonPlaceHolder.putPost("abc", 5, post)

        var call: Call<Post> = jsonPlaceHolder.patchPost(mapOf("Header1" to "1"), 5, post)

        call.enqueue(object: Callback<Post>{
            override fun onFailure(call: Call<Post>, t: Throwable) {
                textView.setText(t.message)
            }

            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if(!response.isSuccessful()){
                    textView.setText("Code: "+ response.code())
                    return
                }
                var postResponse: Post? = response.body()
                var content: String = ""
                content += "Code "+ response.code() + "\n"
                content += "UserId: "+ postResponse!!.userId + "\n"
                content += "Id: "+ postResponse.id + "\n"
                content += "Name: "+ postResponse.title + "\n"
                content += "Text: "+ postResponse.text + "\n\n"
                textView.append(content)
            }
        })
    }

    private fun createPost() {

        //While using @FeildMap
        var map = mapOf<String, String>("userId" to "75", "title" to "New Zealand", "body" to "Planet Earth")
        var call: Call<Post> = jsonPlaceHolder.createPost(map)

        //Used in case of @FormUrlEncoded
//        var call: Call<Post> = jsonPlaceHolder.createPost(89, "Honey Singh", "Great Rapper")

//        var post= Post("New Text", "New Title", 23)
//        var call: Call<Post> = jsonPlaceHolder.createPost(post)

        call.enqueue(object: Callback<Post>{
            override fun onFailure(call: Call<Post>, t: Throwable) {
                textView.setText(t.message)
            }

            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if(!response.isSuccessful()){
                    textView.setText("Code: "+ response.code())
                    return
                }
                var postResponse: Post? = response.body()
                var content: String = ""
                content += "Code "+ response.code() + "\n"
                content += "UserId: "+ postResponse!!.userId + "\n"
                content += "Id: "+ postResponse.id + "\n"
                content += "Name: "+ postResponse.title + "\n"
                content += "Text: "+ postResponse.text + "\n\n"
                textView.append(content)
            }
        })
    }

    private fun getComments() {

        //Use with @Url
        var call : Call<List<Comment>> = jsonPlaceHolder.getComments("posts/3/comments")
        // use without @Url
        // var call : Call<List<Comment>> = jsonPlaceHolder.getComments(3)

        call.enqueue(object : Callback<List<Comment>>{
            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                textView.setText(t.message)
            }

            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                if(!response.isSuccessful()){
                    textView.setText("Code: "+ response.code())
                    return
                }
                var comments : List<Comment>? = response.body()
                if(comments!=null){
                    for(comment in comments){
                        var content: String = ""
                        content += "PostId: "+ comment.postId + "\n"
                        content += "Id: "+ comment.id + "\n"
                        content += "Name: "+ comment.name + "\n"
                        content += "Email: "+ comment.email + "\n"
                        content += "Text: "+ comment.text + "\n\n"
                        textView.append(content)
                    }
                }
            }

        })
    }

    private fun getPost() {

        //use below map method while using @QueryMap
        var parameters =  mapOf("userId" to "1", "_sort" to "id", "_order" to "desc")
        var call : Call<List<Post>> = jsonPlaceHolder.getPosts(parameters)
        // Down method while using @Query
        //var call : Call<List<Post>> = jsonPlaceHolder.getPosts(listOf(4,1), "id", "desc")

        call.enqueue(object : Callback<List<Post>>{
            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                textView.setText(t.message)
                Log.d(TAG, "onFailure: "+t.localizedMessage)
            }

            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if(!response.isSuccessful()){
                    textView.setText("Code: "+ response.code())
                    return
                }
                var posts : List<Post>? = response.body()
                if (posts != null) {
                    for(post in posts){
                        var content: String = ""
                        content += "ID: " + post.id + "\n"
                        content += "UserId: " + post.userId + "\n"
                        content += "Title: " + post.title + "\n"
                        content += "Text: " + post.text + "\n\n"

                        textView.append(content)
                    }
                }
            }

        })
    }
}