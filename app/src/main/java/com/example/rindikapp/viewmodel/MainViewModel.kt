package com.example.rindikapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rindikapp.data.model.Prediction
import com.example.rindikapp.data.repository.MainRepository
import com.example.rindikapp.utils.Constants.POINT_VALUE
import com.example.rindikapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _currentPrediction = MutableLiveData<Resource<Prediction>>(Resource.loading())

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> = _score

    fun setCurrentPrediction(expectedBilah: RequestBody, audio: MultipartBody.Part) {
        viewModelScope.launch {
            _currentPrediction.value = repository.predictBilah(expectedBilah, audio)
            if (_currentPrediction.value?.data != null) {
                if (_currentPrediction.value?.data?.expected == _currentPrediction.value?.data?.result) {
                    _score.value = _score.value?.plus(POINT_VALUE)
                }
            } else {
                _currentPrediction.value = Resource.error("An error has occurred!")
            }
        }
    }
}