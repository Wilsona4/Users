package com.example.users

import com.example.users.data.model.Address
import com.example.users.data.model.Company
import com.example.users.data.model.Geo
import com.example.users.data.model.User
import com.example.users.data.remote.ApiService
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class NetworkDataSourceTest {

    private val mockWebServer by lazy {
        MockWebServer()
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }
    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(mockWebServer.url(""))
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }


    @Test
    fun `should fetch movies given 200 response`() {

        runBlocking {

            mockWebServer.enqueueMockResponse("user-response-200.json", 200)

            val actual = apiService.getUsers()

            assertThat(actual).isNotNull()
        }
    }

    @Test
    fun `should check valid url path`() {
        runBlocking {
            // Prepare fake response
            mockWebServer.enqueueMockResponse("user-response-200.json", 200)
            //Send Request to the MockServer
            val responseBody = apiService.getUsers()
            //Request received by the mock server
            val request = mockWebServer.takeRequest()
            assertThat(responseBody).isNotNull()
            assertThat(request.path).isEqualTo("/users")
        }
    }


    @Test
    fun `should fetch users correctly given 200 response`() {

        runBlocking {
            // Prepare fake response
            mockWebServer.enqueueMockResponse("user-response-200.json", 200)
            //Send Request to the MockServer
            val responseBody = apiService.getUsers()

            val actual = responseBody.first()
            val expected = User(
                id = 1,
                name = "Leanne Graham",
                username = "Bret",
                email = "Sincere@april.biz",
                phone = "1-770-736-8031 x56442",
                website = "hildegard.org",
                imageUri = "",
                company = Company(
                    name = "Romaguera-Crona",
                    bs = "harness real-time e-markets",
                    catchPhrase = "Multi-layered client-server neural-net"
                ),
                address = Address(
                    city = "Gwenborough",
                    street = "Kulas Light",
                    suite = "Apt. 556",
                    zipcode = "92998-3874",
                    geo = Geo(lat = "-37.3159", lng = "81.1496")
                )
            )
            assertThat(actual).isNotNull()
            assertThat(expected.id).isEqualTo(actual.id)
        }
    }


    @After
    fun teardown() {
        mockWebServer.shutdown()
    }
}