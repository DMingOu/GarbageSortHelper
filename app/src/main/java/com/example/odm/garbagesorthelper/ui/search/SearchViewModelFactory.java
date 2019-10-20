package com.example.odm.garbagesorthelper.ui.search;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.odm.garbagesorthelper.model.SearchDataRepository;

/**
 * description: 搜索页面ViewModel工厂类
 * author: ODM
 * date: 2019/10/20
 */
public class SearchViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private SearchDataRepository repository;

    public SearchViewModelFactory(SearchDataRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        T viewModel;
        viewModel = (T)new SearchViewModel(repository);
        return viewModel;
    }
}
