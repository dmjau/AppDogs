package com.cursoandroid.appdogs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

/*
Ejercicio con Retrofit2 y Gson para parceo
Coroutines
RecyclerView
SearchView
Coil
Api usada https://dog.ceo/dog-api/documentation/
*/

//implementamos la interface del SearchView
class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    //Definimos una variable para la URL base
    private val URL_BASE = "https://dog.ceo/api/breed/"

    lateinit var imagesPuppies: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Definimos el escuchador del query
        main_search_breed.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null){
            searchByName(query.toLowerCase(Locale.getDefault()))
        }
        return true
    }

    //No lo usamos porque mira el cambio de texto cada vez
    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    private fun showErrorToast(){
        Toast.makeText(this, getString(R.string.main_error_msg), Toast.LENGTH_SHORT).show()
    }

    private fun getRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun searchByName(query: String) = GlobalScope.launch{
        val call = getRetrofit().create(ApiService::class.java).getDogByBreeds("$query/images").execute()
        val response = call.body() as DogsResponse

        launch(Dispatchers.Main){
            if(response.status == "success"){
                initRecycler(response.images)
            } else {
                showErrorToast()
            }
        }
    }

    //Esto maneja el Recycler
    private fun initRecycler(images: List<String>){
        if(images.isNotEmpty()){
            imagesPuppies = images
        }
        main_recycler.layoutManager = GridLayoutManager(this,2)
        main_recycler.adapter = RecyclerDogAdapter(imagesPuppies)
    }

}