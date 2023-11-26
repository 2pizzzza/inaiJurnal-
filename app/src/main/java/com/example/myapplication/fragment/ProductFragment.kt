package com.example.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.LoginViewModel
import com.example.myapplication.adapter.ProductAdapter
import com.example.myapplication.databinding.FragmentProductBinding
import com.example.myapplication.retrofit.MainApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding
    private val viewModel: LoginViewModel by activityViewModels()
    private lateinit var mainApi: MainApi
    private lateinit var adapter: ProductAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRetrofit()
        initRcView()
        viewModel.token.observe(viewLifecycleOwner) { token ->
            CoroutineScope(Dispatchers.IO).launch {
                val list = mainApi.getAllItems(token)
                requireActivity().runOnUiThread{
                    adapter.submitList(list.products)
                }
            }
        }
    }

    private fun initRetrofit() {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor).build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummyjson.com").client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        mainApi = retrofit.create(MainApi::class.java)
    }

    private fun initRcView() = with(binding){
        adapter = ProductAdapter()
        rcView.layoutManager = LinearLayoutManager(context)
        rcView.adapter = adapter
    }

}