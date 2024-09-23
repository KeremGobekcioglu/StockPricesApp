package com.kg.stockpricesapp.presentation.companyList

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kg.stockpricesapp.domain.repository.StockRepository
import com.kg.stockpricesapp.domain.useCases.GetListingsUseCase
import com.kg.stockpricesapp.domain.useCases.StockUseCases
import com.kg.stockpricesapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

//@HiltViewModel
//class CompanyListingsViewModel @Inject constructor(
//    private val repository: StockRepository
//): ViewModel() {
//
//    var state by mutableStateOf(CompanyListState())
//
//    private var searchJob: Job? = null
//
//    init {
//        getCompanyListings()
//    }
//
//    fun onEvent(event: CompanyListEvents) {
//        when(event) {
//            is CompanyListEvents.Refresh -> {
//                getCompanyListings(fetchFromRemote = true)
//            }
//            is CompanyListEvents.onSearchNameChange -> {
//                state = state.copy(searchName = event.searchName)
//                searchJob?.cancel()
//                searchJob = viewModelScope.launch {
//                    delay(500L)
//                    getCompanyListings()
//                }
//            }
//        }
//    }
//
//    private fun getCompanyListings(
//        query: String = state.searchName.lowercase(),
//        fetchFromRemote: Boolean = false
//    ) {
//        viewModelScope.launch {
//            repository
//                .getListings(fetchFromRemote, query)
//                .collect { result ->
//                    when(result) {
//                        is Resource.Success -> {
//                            result.data?.let { listings ->
//                                state = state.copy(
//                                    companies = listings
//                                )
//                            }
//                        }
//                        is Resource.Error -> Unit
//                        is Resource.Loading -> {
//                            state = state.copy(isLoading = result.isLoading)
//                        }
//                    }
//                }
//        }
//    }
//}

@HiltViewModel
class CompanyListViewModel @Inject constructor(
    private val useCases : StockUseCases
) : ViewModel() {

    // Mutable state to hold the current state of the company list
    private val _state = mutableStateOf(CompanyListState())
    // Publicly exposed immutable state
    val state: State<CompanyListState> = _state

    // Job to handle search debounce
    private var searchJob: Job? = null

    // When we opened the app , listings should be showed. before refreshing ...
    init {
        getCompanies(fetchFromRemote = true)
    }

    // Function to handle different events
    fun onEvent(event: CompanyListEvents) {
        when (event) {
            is CompanyListEvents.Refresh -> {
                // Handle refresh event by fetching companies from remote
                getCompanies(fetchFromRemote = true)
            }
            is CompanyListEvents.onSearchNameChange -> {
                // Update search name in state and debounce search
                _state.value = _state.value.copy(
                    searchName = event.searchName
                )
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)
                    getCompanies(false)
                }
            }
        }
    }

    // Function to fetch companies based on search name and remote flag
    private fun getCompanies(
        fetchFromRemote: Boolean,
        name: String = _state.value.searchName.lowercase()
    ) {
        viewModelScope.launch {
            useCases.getListings(fetchFromRemote, name).collect { result ->
                _state.value = when (result) {
                    is Resource.Success -> {
                        val listings = result.data
                        if (listings.isNullOrEmpty()) {
                            _state.value.copy(
                                companies = emptyList(),
                                isLoading = false,
                                errorMessage = "No companies found matching your search."
                            )
                        } else {
                            _state.value.copy(
                                companies = listings,
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        // Update state with error message on failure
                        _state.value.copy(
                            errorMessage = result.message ?: "An error has occurred",
                            isLoading = false
                        )
                    }
                    is Resource.Loading -> {
                        // Update state to indicate loading
                        _state.value.copy(isLoading = true)
                    }
                }
            }
        }
    }
}