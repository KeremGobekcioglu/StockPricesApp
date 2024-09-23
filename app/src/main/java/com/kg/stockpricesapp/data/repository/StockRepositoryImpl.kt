package com.kg.stockpricesapp.data.repository

import com.kg.stockpricesapp.data.csv.CSVParser
import com.kg.stockpricesapp.data.local.StockDatabase
import com.kg.stockpricesapp.data.local.toCompanyListing
import com.kg.stockpricesapp.data.remote.StockApi
import com.kg.stockpricesapp.data.remote.dto.toCompanyInfo
import com.kg.stockpricesapp.domain.model.CompanyInfo
import com.kg.stockpricesapp.domain.model.CompanyListing
import com.kg.stockpricesapp.domain.model.IntradayInfo
import com.kg.stockpricesapp.domain.model.toCompanyListingEntity
import com.kg.stockpricesapp.domain.repository.StockRepository
import com.kg.stockpricesapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // we will have just one instance of this repository implementation
class StockRepositoryImpl @Inject constructor(
    private val api : StockApi,
    private val db : StockDatabase,
    private val parser : CSVParser<CompanyListing>,
    private val intradayInfoParser : CSVParser<IntradayInfo>
) : StockRepository{

    private val dao = db.stockDao

    /**
     * The method first tries to fetch data from the local database.
     * If the local data is available and fetchFromRemote is false, it emits this data.
     * If the local data is either unavailable or fetchFromRemote
     * is true, it tries to fetch data from the remote API.
     * If the remote data fetch is successful, the local database is updated,
     * and the newly fetched data is emitted.
     * The method uses Flow to handle asynchronous data operations and
     * emit different states (Loading, Success, Error) as the data-fetching process progresses.
     */
    override suspend fun getListings(
        fetchFromRemote: Boolean,
        nameOfCompany: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(true))
            // Fetch the list of company listings from the local database that match the search criteria
            val localList = dao.search(nameOfCompany)
            // Map the list of CompanyListingEntity objects to a list of CompanyListing domain models
            val mappedVersion = localList.map { it.toCompanyListing() }

            if(localList.isNotEmpty())
                emit(Resource.Success(mappedVersion))

            val isDbEmpty = localList.isEmpty() && nameOfCompany.isBlank()
            val fromCache = !isDbEmpty && !fetchFromRemote

            if(fromCache)
            {
                emit(Resource.Loading(false))
                return@flow
            }
            //bura bos geliyor
            val remoteListings = try {
                val response = api.getStocks()
                parser.parse(response.byteStream())
            }
            catch (e: HttpException)
            {
                e.printStackTrace()
                emit(Resource.Error("Could not load the data. HTTPEXCEPTION"))
                null
            }

            catch (e : IOException)
            {
                e.printStackTrace()
                emit(Resource.Error("Something happened. IOEXCEPTION "))
                null
            }
//            remoteListings?. let { listings ->
//                dao.clearListings()
//                dao.insertCompanyListings(
//                    listings.map { it.toCompanyListingEntity() })
//
//                emit(Resource.Success(
//                    data = dao
//                        .search("")
//                        .map { it.toCompanyListing()}
//                ))
//                emit(Resource.Loading(false))
//            }
            if(remoteListings!!.isEmpty())
            {
                emit(Resource.Error("bos geliypr"))
            }
            else
            {
                dao.clearListings()
                dao.insertCompanyListings(
                    remoteListings.map { it.toCompanyListingEntity() }
                )

                emit(Resource.Success(
                    data = dao
                        .search("")
                        .map { it.toCompanyListing()}
                ))
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> {
        return try {
            val result = api.getCompanyInfo(symbol)
            val toModel = result.toCompanyInfo()
            Resource.Success(toModel)
        }
        catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(
                message = "Network or I/O error occurred while fetching company info."
            )
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(
                message = "HTTP error occurred while fetching company info."
            )
        }
    }

    override suspend fun getIntradayInfo(symbol: String): Resource<List<IntradayInfo>> {
        return try {
            val response = api.getIntradayInfo(symbol)
            // api brings response body but our parser takes input stream
            val results = intradayInfoParser.parse(response.byteStream())
            Resource.Success(results)
        }
    catch (e: IOException) {
        e.printStackTrace()
        Resource.Error(
            message = "Network or I/O error occurred while fetching intraday info."
        )
    } catch (e: HttpException) {
        e.printStackTrace()
        Resource.Error(
            message = "HTTP error occurred while fetching intraday info."
        )
    }
    }
}