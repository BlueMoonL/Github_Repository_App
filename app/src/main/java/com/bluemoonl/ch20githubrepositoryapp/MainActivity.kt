package com.bluemoonl.ch20githubrepositoryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isGone
import com.bluemoonl.ch20githubrepositoryapp.data.database.DataBaseProvider
import com.bluemoonl.ch20githubrepositoryapp.data.entity.GithubOwner
import com.bluemoonl.ch20githubrepositoryapp.data.entity.GithubRepoEntity
import com.bluemoonl.ch20githubrepositoryapp.databinding.ActivityMainBinding
import com.bluemoonl.ch20githubrepositoryapp.view.RepositoryRecyclerAdapter
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: RepositoryRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapter()
        initViews()
    }

    private fun initAdapter() {
        adapter = RepositoryRecyclerAdapter()
    }

    private fun initViews() = with(binding) {
        emptyResultTextView.isGone = true
        recyclerView.adapter = adapter
        searchButton.setOnClickListener {
            startActivity(
                Intent(this@MainActivity, SearchActivity::class.java)
            )
        }
    }

    override fun onResume() {
        super.onResume()
        launch(coroutineContext) {
            loadSearchHistory()
        }
    }

    private suspend fun loadSearchHistory() = withContext(Dispatchers.IO) {
        val histories = DataBaseProvider.provideDB(this@MainActivity).searchHistoryDao().getHistory()
        withContext(Dispatchers.Main) {
            setData(histories)
        }
    }

    private fun setData(githubRepoList: List<GithubRepoEntity>) = with(binding) {
        if (githubRepoList.isEmpty()) {
            emptyResultTextView.isGone = false
            recyclerView.isGone = true
        } else {
            emptyResultTextView.isGone = true
            recyclerView.isGone = false
            adapter.setSearchResultList(githubRepoList) {
                startActivity(
                    Intent(this@MainActivity, RepositoryActivity::class.java).apply {
                        putExtra(RepositoryActivity.REPOSITORY_OWNER_KEY, it.owner.login)
                        putExtra(RepositoryActivity.REPOSITORY_NAME_KEY, it.name)
                    }
                )
            }
        }
    }

}