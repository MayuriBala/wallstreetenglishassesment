package com.example.thirukuralapp.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.example.thirukuralapp.NetworkUtil.InternetCheck
import com.example.thirukuralapp.R
import com.example.thirukuralapp.Utils.Constants
import com.example.thirukuralapp.databinding.ActivityScrollingBinding
import com.example.thirukuralapp.interfaces.OnClickListenerForGettingData
import com.example.thirukuralapp.viewmodel.ThirukuralViewmodel
import com.google.android.material.appbar.CollapsingToolbarLayout


class ThirukuralActivity : AppCompatActivity(),OnClickListenerForGettingData {

    lateinit var binding: ActivityScrollingBinding
    var thiruKuralViewModel: ThirukuralViewmodel = ThirukuralViewmodel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * Databinding initialization
         * */
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scrolling)
        binding.lifecycleOwner = this
        binding.includeContentLay.callback = this
        thiruKuralViewModel.getInstance(this)

        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title = title

        InternetCheck(object : InternetCheck.Consumer {
            override fun accept(internet: Boolean?) {
                if(internet == true){
                    getThiruKural(Constants.INITIAL_VALUE)
                }else{
                    binding.includeContentLay.nestedScrollView.visibility = View.GONE
                    Toast.makeText(applicationContext,getString(R.string.internet_check),Toast.LENGTH_SHORT).show()
                }
            }
        })
    }



    /**
     * API call for getting thirukural info
     * */
    private fun getThiruKural(num: Int) {
        thiruKuralViewModel.getThirukural(num).observe(
            this
        ) {
            it.onSuccess { it ->
                it.let {
                    binding.includeContentLay.data = it
                    thiruKuralViewModel.saveValue(it.number)
                }
            }
            it.onFailure {
                Toast.makeText(this, R.string.no_data_found, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Onclick for getting particular thirukural button func
     * */
    override fun getParticularThiruKural() {
        if (binding.includeContentLay.inputNoTv.text!!.isNotEmpty()) {
            if (binding.includeContentLay.inputNoTv.text.toString().toInt() < Constants.MAX_VALUE ) {
                getThiruKural(binding.includeContentLay.inputNoTv.text.toString().toInt())
            } else {
                binding.includeContentLay.inputNoTv.error = getString(R.string.hint)
            }
        } else {
            binding.includeContentLay.inputNoTv.error = getString(R.string.error_msg)
        }
    }

    /**
     * Onclick for next button func
     * */
    override fun nextBtnClick() {
        getThiruKural(thiruKuralViewModel.fetchValue()+1)
    }

    /**
     * Onclick for previous button func
     * */
    override fun prevBtnClick() {
        if (thiruKuralViewModel.fetchValue() > 1) {
            getThiruKural(thiruKuralViewModel.fetchValue()-1)
        }
    }
}