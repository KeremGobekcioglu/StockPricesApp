package com.kg.stockpricesapp.presentation.companyInfo

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kg.stockpricesapp.domain.repository.StockRepository
import com.kg.stockpricesapp.domain.useCases.StockUseCases
import com.kg.stockpricesapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject
//@HiltViewModel
//class CompanyInfoViewModel @Inject constructor(
//    private val savedStateHandle: SavedStateHandle,
//    private val repository: StockRepository
//): ViewModel() {
//
//    var state by mutableStateOf(CompanyInfoState())
//
//    init {
//        viewModelScope.launch {
//            val symbol = savedStateHandle.get<String>("symbol") ?: return@launch
//            state = state.copy(isLoading = true)
//            val companyInfoResult = async { repository.getCompanyInfo(symbol) }
//            val intradayInfoResult = async { repository.getIntradayInfo(symbol) }
//            when(val result = companyInfoResult.await()) {
//                is Resource.Success -> {
//                    state = state.copy(
//                        company = result.data,
//                        isLoading = false,
//                        errorMessage = null
//                    )
//                }
//                is Resource.Error -> {
//                    state = state.copy(
//                        isLoading = false,
//                        errorMessage = result.message,
//                        company = null
//                    )
//                }
//                else -> Unit
//            }
//            when(val result = intradayInfoResult.await()) {
//                is Resource.Success -> {
//                    state = state.copy(
//                        stockInfos = result.data ?: emptyList(),
//                        isLoading = false,
//                        errorMessage = null
//                    )
//                }
//                is Resource.Error -> {
//                    state = state.copy(
//                        isLoading = false,
//                        errorMessage = result.message,
//                        company = null
//                    )
//                }
//                else -> Unit
//            }
//        }
//    }
//}
@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val useCases: StockUseCases,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(CompanyInfoState())
    val state: State<CompanyInfoState> = _state

    init {
        viewModelScope.launch {
            val symbol = savedStateHandle.get<String>("symbol") ?: return@launch
            _state.value = _state.value.copy(isLoading = true)

            // Api calls will be make simultaneously so that it will be quicker that is why we used async
            val companyInfoResult = async { useCases.getCompanyInfo(symbol) }
            val intradayInfoResult = async { useCases.getIntradayInfo(symbol) }

            // Collect the results
            val companyInfo = companyInfoResult.await()
            val intradayInfo = intradayInfoResult.await()

            // Update the state based on results
            _state.value = _state.value.copy(
                company =
                if (companyInfo is Resource.Success)
                    companyInfo.data
                else null,

                stockInfos =
                    if (intradayInfo is Resource.Success)
                        intradayInfo.data ?: emptyList()
                    else emptyList(),

                isLoading = false,

                errorMessage = when {
                    companyInfo is Resource.Error ->
                        companyInfo.message ?: "An error has occurred"

                    intradayInfo is Resource.Error ->
                        intradayInfo.message ?: "An error has occurred"

                    else -> null
                }
            )
        }
    }
}

//
//@HiltViewModel
//class CompanyInfoViewModel @Inject constructor(
//    private val useCases : StockUseCases,
//    private val savedStateHandle: SavedStateHandle
//) : ViewModel() {
//
//    private val _state = mutableStateOf(CompanyInfoState())
//    val state : State<CompanyInfoState> = _state
//
//    init {
//        viewModelScope.launch {
//            // we need symbol to make the api call so we get it from savedstatehandle
//            val symbol = savedStateHandle.get<String>("symbol") ?: return@launch
//            _state.value = _state.value.copy(isLoading = true)
//            // Api calls will be make simultaneously so that it will be quicker that is why we used async
//            val companyInfoResult = async {  useCases.getCompanyInfo(symbol) }
//            val intradayInfoResult = async {useCases.getIntradayInfo(symbol) }
//
//            _state.value = when(val result = companyInfoResult.await())
//            {
//                is Resource.Success ->
//                {
//                    _state.value.copy(
//                        company = result.data,
//                        isLoading = false,
//                        errorMessage = null
//                    )
//                }
//                is Resource.Error ->
//                {
//                    _state.value.copy(
//                        company = null,
//                        errorMessage = result.message ?: "An error has occurred",
//                        isLoading = false
//                    )
//                }
//                is Resource.Loading ->
//                {
//                    _state.value.copy(
//                        isLoading = true
//                    )
//                }
//            }
//
//            _state.value = when(val result = intradayInfoResult.await())
//            {
//                is Resource.Success ->
//                {
//                    _state.value.copy(
//                        stockInfos = result.data ?: emptyList(),
//                        isLoading = false,
//                        errorMessage = null
//                    )
//                }
//                is Resource.Error ->
//                {
//                    _state.value.copy(
//                        stockInfos = emptyList(),
//                        errorMessage = result.message ?: "An error has occurred",
//                        isLoading = false,
//                        company = null
//                    )
//                }
//                is Resource.Loading ->
//                {
//                    _state.value.copy(
//                        isLoading = true
//                    )
//                }
//            }
//        }
//    }
//
//}