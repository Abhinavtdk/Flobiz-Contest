package com.example.flobizcontest.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flobizcontest.service.Resource
import com.example.flobizcontest.model.StackExchangeResponse
import com.example.flobizcontest.repository.StackExchangeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class StackExchangeViewModel @Inject internal constructor(private val stackExchangeRepository: StackExchangeRepository) :
    ViewModel() {

    private val _questions = MutableLiveData<Resource<StackExchangeResponse>>()
    val questions: LiveData<Resource<StackExchangeResponse>> get() = _questions

    private val _searchedQuestions: MutableLiveData<Resource<StackExchangeResponse>> =
        MutableLiveData()
    val searchedQuestions: LiveData<Resource<StackExchangeResponse>> = _searchedQuestions

    private val _taggedQuestions: MutableLiveData<Resource<StackExchangeResponse>> =
        MutableLiveData()
    val taggedQuestions: LiveData<Resource<StackExchangeResponse>> = _taggedQuestions


    init {
        getActiveQuestions()
    }

    fun getActiveQuestions() = viewModelScope.launch {
        _questions.postValue(Resource.Loading())
        Log.d("ViewModelHey", "getActiveQuestions: Status=${_questions.value}")
        val response = stackExchangeRepository.fetchQuestions()
        _questions.postValue(safeHandleResponse(response))
        Log.d("ViewModelHey", "getActiveQuestions: Status=${questions.value} + ${response.body()}")
    }

    private suspend fun safeHandleResponse(response: Response<StackExchangeResponse>): Resource<StackExchangeResponse>? {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(data = response.body()!!)
            } catch (e: Exception) {
                Resource.Error(e.message.toString())
            }
        }
    }

    fun searchQuestions(query: String) = viewModelScope.launch {
        _searchedQuestions.postValue(Resource.Loading())
        val response = stackExchangeRepository.searchQuestions(query)
        _searchedQuestions.postValue(safeHandleSearchResponse(response))
    }

    private suspend fun safeHandleSearchResponse(response: Response<StackExchangeResponse>): Resource<StackExchangeResponse>? {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(data = response.body()!!)
            } catch (e: Exception) {
                Resource.Error(e.message.toString())
            }
        }
    }

    fun searchWithFilterTags(tags: String) = viewModelScope.launch {
        _taggedQuestions.postValue(Resource.Loading())
        val response = stackExchangeRepository.searchWithFilterTags(tags)
        _taggedQuestions.postValue(safeHandleTaggedResponse(response))
    }

    private suspend fun safeHandleTaggedResponse(response: Response<StackExchangeResponse>): Resource<StackExchangeResponse>? {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(data = response.body()!!)
            } catch (e: Exception) {
                Resource.Error(e.message.toString())
            }
        }
    }


}