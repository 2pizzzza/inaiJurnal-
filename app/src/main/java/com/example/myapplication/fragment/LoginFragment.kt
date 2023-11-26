package com.example.myapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.adapter.LoginViewModel
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentLoginBinding
import com.example.myapplication.entity.AuthRequest
import com.example.myapplication.retrofit.MainApi
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by activityViewModels()
    private lateinit var mainApi: MainApi

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRetrofit()
        binding.apply {
            bnext.setOnClickListener {
                findNavController().navigate(R.id.action_LoginFragment_to_ProductFragment)

            }

            loginB.setOnClickListener {
                auth(
                    AuthRequest(
                        loginField.text.toString(),
                        passwordField.text.toString()
                    )
                )
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

    private fun auth(authRequest: AuthRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = mainApi.auth(authRequest)
            val message = response.errorBody()?.string()?.let {
                JSONObject(it).getString("message")
            }
            requireActivity().runOnUiThread {
                binding.errorField.text = message
                val user = response.body()
                if (user != null) {
                    Picasso.get().load(user.image).into(binding.imageView2)
                    binding.nameFields.text = user.username
                    binding.bnext.visibility = View.VISIBLE
                    viewModel.token.value = user.token
                }
            }
        }
    }
}