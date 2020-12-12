package com.anasely.zoomzoom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide

import org.json.JSONException
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // hnaya initializit l buttona , dertha f variable smito buttonSearch , bash nkhdm biha mn ba3d
        var buttonSearch =findViewById<Button>(R.id.searchButton)


        // hnaya fash nabta 3la buttona radi t exucuta function li smitha getPic
        buttonSearch.setOnClickListener{

            getPic()
        }


    }

    private fun getPic() {
        // hadi progress bar initializitha o dertha f variable , raha darga f activity main db
        var progressBar=findViewById<ProgressBar>(R.id.progressBar)
        // ster 41 dert variable bash n initializi dak editeText li radi ydkhl fih bnadem user name
        var editText=findViewById<EditText>(R.id.getUserName)

        // hnaya khdit username  mn 3end bnadem o stockito f variable li smito userName2222222222222
        var userName=editText.text.toString()

        // hnaya t checkit wash bnadem madakhal walo za3ma btta 3la buttona o ma3ati ta userName
        if(userName.isEmpty()){
            Toast.makeText(this,"Write an username to find his picture",Toast.LENGTH_LONG).show()
        }else{

            // hnaya progress bar dertha tban hit hna bda search
            progressBar.visibility=View.VISIBLE
            var imageToHide=findViewById<ImageView>(R.id.imageView)
            imageToHide.visibility=View.GONE
            // hnaya radi n jbad Json mn api
            Thread(Runnable {
                // This part to get the JSON from the profile of Instagram
                val builder = StringBuilder()
                try {

                    // hada lien li anjbdo mno l json o dik userName hia li dakhal bnadem , raha f ster 44
                    val url = "https://www.instagram.com/"+userName+"/?__a=1"
                    val doc: Document? = Jsoup
                        .connect(url)
                        .ignoreContentType(true)
                        .get()
                    val body: Element? = doc?.body()
                    if (body != null) {
                        builder.append(body.text())
                    }
                } catch (e: Exception) {
                    e.printStackTrace();
                }
                runOnUiThread {
                    // This is the important part we got the Json from previous part now we are going to extract
                    // informations from this Json ..


                    try {

                        // had partie biha anjbad taswira , kadkhol haja wst haja , jbdna f lwl jsonObjet( ster 79) , o bih dkhlna l graph ster (81)
                        // o b graph dkhlna west user , o mn west user jbadna lien dyal tswira
                        val jsonObj =
                            JSONObject(builder.toString()) // Get the full Json object
                        val graphql =
                            jsonObj.getJSONObject("graphql") // Get the graphq1 object
                        val user =
                            graphql.getJSONObject("user") // Get the user object from graphq1 object

                        if (!user.toString().isEmpty()) {
                         //   hna dert progress bar dreg hit l9ina tswira dyal user
                            progressBar.visibility=View.GONE


                            // hna variable smito profile picture wasto rani m stocker lien dyal image
                            var profile_picture=user.getString("profile_pic_url_hd")


                            // hnaya dert variable intializit wsto tswira li raha 3endi f activity_main.xml bash nkhdm biha mn ba3d
                            var image=findViewById<ImageView>(R.id.imageView)

                            // hnaya khdamt b library glide bash tswira li raha f lien f variable li f ster 93 bash n7tha f view dyali
                            Glide.with(this).load(profile_picture).into(image);

                            imageToHide.visibility=View.VISIBLE
                        }


                        // Show the result layout and hide the search layout
                    } catch (e: JSONException) {
                        Toast.makeText(
                            this@MainActivity,
                            "${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }).start()
        }


    }
}


