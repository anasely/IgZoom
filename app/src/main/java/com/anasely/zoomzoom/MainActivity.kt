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


        //ici j'ai initialisé button de recherche et je mis ce dernier dans une variable <button_Search>
        var buttonSearch =findViewById<Button>(R.id.searchButton)



        /
        //Quand j'appuye sur le button la fonction getpic s'exécuter.
        buttonSearch.setOnClickListener{

            getPic()
        }


    }

    private fun getPic() {  //Création une function getPic()


        // J'ai initialisé  barre de progression dans  variable ProgressBar.
        var progressBar=findViewById<ProgressBar>(R.id.progressBar)
        // J'ai initialisé EditText dans variable EditText c'est  la zone ou dans le user entrer le username
        var editText=findViewById<EditText>(R.id.getUserName)
        //J'ai créé une variable dans laquelle je stocke le nom d'utilisateur saisi par l'utilisateur
        var userName=editText.text.toString()

        // Pour vérifier si l'utilisateur a appuyé sur le bouton de recherche
        // sans entrer le nom d'utilisateur dans la zone de texte
        if(userName.isEmpty()){
            Toast.makeText(this,"Entre le nom d'utilisateur pour trouver leur photo ",Toast.LENGTH_LONG).show()
        }else{

            // J'ai donne l'accord pour que la progressbar être visible parceque en a commence la recherche
            progressBar.visibility=View.VISIBLE
            var imageToHide=findViewById<ImageView>(R.id.imageView)
            imageToHide.visibility=View.GONE


            // Je récupere json a partir d'api
            Thread(Runnable {
                // Cette partie pour obtenir le JSON à partir du profil d'Instagram
                val builder = StringBuilder()
                try {

                    //c'est le lien qui on va obtenir json A partir de ce lien on va obtenire json
                    val url = "https://www.instagram.com/"+userName+"/?__a=1" //username c'est le nom d'utilisateur entrer par l'utilisateur.


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
                // C'est la partie importante que nous avons obtenu le Json de la partie
                // précédente maintenant que nous allons extraire informations de ce Json ..
                // Fondamentalement, nous avons obtenu le résultat mais et en arrière-plan maintenant nous allons l'afficher directement


                    try {

                        //en effet la methode de obtenir le username c'est comme l'heritage
                        // en a dans l'interieur de jsonobejct le graph et dans le graph on a user
                        // et sur le user en peut accede a les proprièté de ce user comme le lien de la photo de profil...

                        val jsonObj =
                            JSONObject(builder.toString()) // Obtenez l'objet Json complet
                        val graphql =
                            jsonObj.getJSONObject("graphql") //Récupère l'objet graphq1
                        val user =
                            graphql.getJSONObject("user") // Récupère l'objet utilisateur de l'objet graphq1

                        if (!user.toString().isEmpty()) {
                          //j'ai cache la progressBar parceque on a touver la photo
                            progressBar.visibility=View.GONE


                            //j'ai crée une variable profil_picture dans le quel j'ai stocke lien d'image
                            var profile_picture=user.getString("profile_pic_url_hd")


                            //j'ai inistialise  l'imageview dans variable image
                            var image=findViewById<ImageView>(R.id.imageView)

                            //J'ai utilisé la bibliothèque glide pour obtenir le lien stocké
                            // dans profil_picture et le mettre dans mon view
                            Glide.with(this).load(profile_picture).into(image);

                            imageToHide.visibility=View.VISIBLE //pour affiche la photo
                        }


                        // Afficher la mise en page des résultats et masquer la mise en page de recherche
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


