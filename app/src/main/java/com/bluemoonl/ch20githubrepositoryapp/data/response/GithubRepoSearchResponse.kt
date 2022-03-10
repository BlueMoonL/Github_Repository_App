package com.bluemoonl.ch20githubrepositoryapp.data.response

import com.bluemoonl.ch20githubrepositoryapp.data.entity.GithubRepoEntity

data class GithubRepoSearchResponse(
    val totalCount: Int,
    val items: List<GithubRepoEntity>
)