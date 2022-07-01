package com.example.project_2

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*
import com.android.volley.toolbox.JsonObjectRequest as JsonObjectRequest1

class MainActivity : AppCompatActivity() {
    var currentImageurl :String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         loadMeme()
    }

    private fun loadMeme() {
        progressBar.visibility= View.VISIBLE        // due to this our progress bar will be visible when ever the loadmeme fucntion is call
        val url = "https://meme-api.herokuapp.com/gimme"

        val jsonObjectRequest = JsonObjectRequest1(
        Request.Method.GET, url, null ,
            Response.Listener{ response ->
               currentImageurl= response.getString( "url")
                Glide.with(this).load(currentImageurl).listener(object: RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility=View.GONE // IF IMAGE fails to load then the progress bar will vanish
                        return false
                    }
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility=View.GONE // similarly even if new image gets loaded the progress bar should go
                        return false
                    }

                }).into(imageView)
             },
            {
             Toast.makeText(this,"something went wrong" , Toast.LENGTH_LONG).show()
            })
        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    fun shareMeme(view: android.view.View) {  // this will get called when share button is clicked
    val intent = Intent(Intent.ACTION_SEND)
        intent.type="text/plain" // due to this when you click on share button whayever apps are present on you phone will be shown ex:insta,whatsapp,facebook
        intent.putExtra(Intent.EXTRA_TEXT, "hey checkout this meme $currentImageurl") // when we will press the share button a tect will show
    val chooser=Intent.createChooser(intent,"share this meme")
        startActivity(chooser)
    }
    fun nextMeme(view: android.view.View)
    {             // this fucntion will get called when next button is called
      loadMeme()
    }
}