package com.kg.stockpricesapp.presentation.companyList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.kg.stockpricesapp.presentation.companyInfo.CompanyInfoScreen
import com.kg.stockpricesapp.presentation.destinations.CompanyInfoScreenDestination

@Composable
@Destination(start = true)
fun CompanyListScreen(
    navigator: DestinationsNavigator,
    viewModel: CompanyListViewModel = hiltViewModel()
)
{
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = viewModel.state.value.isRefreshing
    )
    val state = viewModel.state.value
    Column(
        modifier = Modifier.fillMaxSize()
    )
    {
        OutlinedTextField(
            value = state.searchName, // Binds the text field's value to the searchName in the state
            onValueChange = { // Called when the text in the field changes
                viewModel.onEvent(CompanyListEvents.onSearchNameChange(it)) // Updates the searchName in the ViewModel
            },
            modifier = Modifier
                .padding(16.dp) // Adds padding around the text field
                .fillMaxWidth(), // Makes the text field fill the width of its parent
            placeholder = {
                Text(text = "Search ...") // Displays placeholder text when the field is empty
            },
            maxLines = 1, // Limits the text field to a single line
            singleLine = true // Ensures the text field behaves as a single-line input
        )
        if (!state.errorMessage.isNullOrEmpty()) {
            Text(
                text = state.errorMessage + " naber ",
                modifier = Modifier.padding(16.dp)
            )
        }
        SwipeRefresh(state = swipeRefreshState,
            onRefresh = { viewModel.onEvent(CompanyListEvents.Refresh) })
        {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            )
            {
                items(state.companies.size)
                {
                    i ->
                    val company = state.companies[i]
                    CompanyItem(
                        company = company,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navigator.navigate(CompanyInfoScreenDestination(company.symbol))
                            }
                            .padding(16.dp)
                    )
                    if(i < state.companies.size) // Last item should not have a divider
                    {
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }
        }
    }
}