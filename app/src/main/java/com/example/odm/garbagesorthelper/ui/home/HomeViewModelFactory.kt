package com.example.odm.garbagesorthelper.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.odm.garbagesorthelper.model.SearchDataRepository

/**
 * @description: 主页面ViewModel工厂
 * @author: ODM
 * @date: 2020/1/15
 */
class HomeViewModelFactory (private val repository: SearchDataRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel: T

        viewModel = HomeViewModel(repository) as T
        return viewModel
    }
}