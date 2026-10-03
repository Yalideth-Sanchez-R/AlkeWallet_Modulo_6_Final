package com.example.alkewallet.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AlkeWalletCliente {

    // URL de la api ficticia de tu Beeceptor
    private const val BASE_URL = "https://alkewalletapi.free.beeceptor.com"

    val apiService: AlkeWalletApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AlkeWalletApi::class.java)
    }
}
