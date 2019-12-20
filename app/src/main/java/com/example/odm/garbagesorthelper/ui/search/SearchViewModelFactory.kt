package com.example.odm.garbagesorthelper.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.example.odm.garbagesorthelper.base.BaseRepository
import com.example.odm.garbagesorthelper.model.SearchDataRepository

/**
 * description: 搜索页面ViewModel工厂类
 * author: ODM
 * date: 2019/10/20
 */
class SearchViewModelFactory(private val repository: SearchDataRepository) : NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel: T

        viewModel = SearchViewModel(repository) as T
        return viewModel
    }

}