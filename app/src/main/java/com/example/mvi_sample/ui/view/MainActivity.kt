package com.example.mvi_sample.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvi_sample.R
import com.example.mvi_sample.data.api.ApiHelperImpl
import com.example.mvi_sample.data.api.RetrofitBuilder
import com.example.mvi_sample.data.model.User
import com.example.mvi_sample.databinding.ActivityMainBinding
import com.example.mvi_sample.ui.adapter.MainAdapter
import com.example.mvi_sample.ui.intent.MainIntent
import com.example.mvi_sample.ui.viewModel.MainViewModel
import com.example.mvi_sample.ui.viewState.MainState
import com.example.mvi_sample.util.ViewModelFactory
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private var adapter = MainAdapter(arrayListOf())

    private var _binding: ActivityMainBinding? = null
    private val viewBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        setupUI()
        setupViewModel()
        observeViewModel()
        setupClicks()
        print("change1")
    }

    private fun setupClicks() {
        viewBinding.buttonFetchUser.setOnClickListener {
            lifecycleScope.launch {
                mainViewMuserIntent.send(MainIntent.FetchUser)
            }
        }
    }

    private fun setupUI() {
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        viewBinding.recyclerView.run {
            addItemDecoration(
                DividerItemDecoration(
                    viewBinding.recyclerView.context,
                    (viewBinding.recyclerView.layoutManager as LinearLayoutManager).orientation
                )
            )
        }
        viewBinding.recyclerView.adapter = adapter
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelperImpl(
                    RetrofitBuilder.apiService
                )
            )
        )[MainViewModel::class.java]
    }


    private fun observeViewModel() {
        lifecycleScope.launch {
            mainViewModel.state.collect {
                when (it) {
                    is MainState.Idle -> {
                        Log.i("state","Idle is  ....")

                    }
                    is MainState.Loading -> {
                        viewBinding.buttonFetchUser.visibility = View.GONE
                        viewBinding.progressBar.visibility = View.VISIBLE
                        Log.i("state","loading is  ....")
                    }

                    is MainState.Users -> {
                        viewBinding.progressBar.visibility = View.GONE
                        viewBinding.buttonFetchUser.visibility = View.GONE
                        renderList(it.user)
                    }
                    is MainState.Error -> {
                        viewBinding.progressBar.visibility = View.GONE
                        viewBinding.buttonFetchUser.visibility = View.VISIBLE
                        Toast.makeText(this@MainActivity, it.error, Toast.LENGTH_LONG).show()
                        Log.i("state","error is  ....")

                    }
                }
            }
        }
    }


    private fun renderList(users: List<User>) {
        viewBinding.recyclerView.visibility = View.VISIBLE
        users.let { listOfUsers -> listOfUsers.let { adapter.addData(it) } }
        adapter.notifyDataSetChanged()
    }




    override fun onDestroy() {
        super.onDestroy()
        _binding = null;
    }
}