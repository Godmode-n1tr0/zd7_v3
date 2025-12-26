package com.bignerdranch.android.pract7.data

import retrofit2.http.GET

interface FurnitureApi {
    @GET("furniture_types")
    suspend fun getFurnitureTypes(): List<FurnitureTypeResponse>
}
