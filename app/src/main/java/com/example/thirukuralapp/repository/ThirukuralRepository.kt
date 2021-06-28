package com.example.thirukuralapp.repository

import com.example.thirukuralapp.api.APIService
import com.example.thirukuralapp.model.ThirukuralResponse

class ThirukuralRepository {
    suspend fun getThirukural(num: Int): ThirukuralResponse {
        return APIService.create().getThirukural(num)
    }
}