package com.example.thirukuralapp.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.preference.PreferenceManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.thirukuralapp.model.ThirukuralResponse
import com.example.thirukuralapp.repository.ThirukuralRepository

class ThirukuralViewmodel : ViewModel() {
    var thiruKuralRepository: ThirukuralRepository = ThirukuralRepository()
    lateinit var thisContext: Context
    var position = 0

    /**
     * Getting instance
     * */
    fun getInstance(context : Context){
        this.thisContext = context
    }

    /**
     * API call for getting data
     * */
    fun getThirukural(num: Int) = liveData {
        try {
            val response = thiruKuralRepository.getThirukural(num)
            emit(Result.success(response))
        } catch (cause: Throwable) {
            emit(Result.failure<ThirukuralResponse>(cause))
        }
    }

    /**
     * Saving value from shared preference
     * */
    fun saveValue(num: Int){
        val preferences = PreferenceManager.getDefaultSharedPreferences(thisContext)
        val editor = preferences.edit()
        editor.putInt("position", num)
        editor.apply()
    }

    /**
     * Getting value from shared preference
     * */
    fun fetchValue() :Int{
        val preferences = PreferenceManager.getDefaultSharedPreferences(thisContext)
        var num = preferences.getInt("position", 0)
        if (num != 0) {
            position = num /* Edit the value here*/
        }
        return position
    }

    /**
     * Checking internet connection
     * */
    @SuppressLint("ServiceCast")
    fun isNetworkAvailable(): Boolean {
        val cm = thisContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetworkInfo: NetworkInfo? = null
        activeNetworkInfo = cm.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }
}