package br.com.alura.service

import br.com.alura.model.Top250Data
import br.com.alura.webclient.IMDB_API_KEY
import retrofit2.Call
import retrofit2.http.GET

interface MovieService {
    @GET(IMDB_API_KEY)
    fun findTop250Movies(): Call<Top250Data>
}
