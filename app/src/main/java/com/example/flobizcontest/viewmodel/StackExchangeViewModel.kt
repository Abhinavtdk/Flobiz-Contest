package com.example.flobizcontest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flobizcontest.Resource
import com.example.flobizcontest.model.StackExchangeResponse
import com.example.flobizcontest.repository.StackExchangeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class StackExchangeViewModel @Inject constructor(val stackExchangeRepository: StackExchangeRepository) : ViewModel() {

    private val _questions : MutableLiveData<Resource<StackExchangeResponse>> = MutableLiveData()
    val questions : LiveData<Resource<StackExchangeResponse>> = _questions

    private val _searchedQuestions : MutableLiveData<Resource<StackExchangeResponse>> = MutableLiveData()
    val searchedQuestions : LiveData<Resource<StackExchangeResponse>> = _searchedQuestions

    private val _taggedQuestions : MutableLiveData<Resource<StackExchangeResponse>> = MutableLiveData()
    val taggedQuestions : LiveData<Resource<StackExchangeResponse>> = _taggedQuestions


    init {
        getActiveQuestions()
    }

    private fun getActiveQuestions() = viewModelScope.launch {
        _questions.postValue(Resource.Loading())
        val response = stackExchangeRepository.fetchQuestions()
        _questions.postValue(safeHandleResponse(response))
    }

    private suspend fun safeHandleResponse(response: Response<StackExchangeResponse>): Resource<StackExchangeResponse>? {
        return withContext(Dispatchers.IO){
            try {
                Resource.Success(data = response.body()!!)
            } catch (e: Exception){
                Resource.Error(e.message.toString())
            }
        }
    }

    private fun searchQuestions(query: String) = viewModelScope.launch {
        _searchedQuestions.postValue(Resource.Loading())
        val response = stackExchangeRepository.searchQuestions(query)
        _searchedQuestions.postValue(safeHandleSearchResponse(response))
    }

    private suspend fun safeHandleSearchResponse(response: Response<StackExchangeResponse>): Resource<StackExchangeResponse>? {
        return withContext(Dispatchers.IO){
            try {
                Resource.Success(data = response.body()!!)
            } catch (e: Exception){
                Resource.Error(e.message.toString())
            }
        }
    }

    private fun searchWithFilterTags(tags: String) = viewModelScope.launch {
        _taggedQuestions.postValue(Resource.Loading())
        val response = stackExchangeRepository.searchWithFilterTags(tags)
        _taggedQuestions.postValue(safeHandleTaggedResponse(response))
    }

    private suspend fun safeHandleTaggedResponse(response: Response<StackExchangeResponse>): Resource<StackExchangeResponse>? {
        return withContext(Dispatchers.IO){
            try {
                Resource.Success(data = response.body()!!)
            } catch (e: Exception){
                Resource.Error(e.message.toString())
            }
        }
    }





}