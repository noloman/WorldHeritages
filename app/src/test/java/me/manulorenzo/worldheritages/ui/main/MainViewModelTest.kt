package me.manulorenzo.worldheritages.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import me.manulorenzo.worldheritages.CoroutinesTestRule
import me.manulorenzo.worldheritages.Faker
import me.manulorenzo.worldheritages.data.Resource
import me.manulorenzo.worldheritages.data.di.repositoryModule
import me.manulorenzo.worldheritages.data.model.Heritage
import me.manulorenzo.worldheritages.data.source.HeritageResponse
import me.manulorenzo.worldheritages.data.source.Repository
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject
import org.koin.test.mock.declareMock

@ExperimentalCoroutinesApi
class MainViewModelTest : AutoCloseKoinTest() {
    private val repositoryMock: Repository by inject()
    @get:Rule
    val rule = InstantTaskExecutorRule()
    @get:Rule
    val coroutineTestRule = CoroutinesTestRule()

    @Test
    fun `given a fake of a list of heritages it should first emit loading and then success`() =
        runBlockingTest {
            val fakeSuccessResource =
                Resource.Success(Faker.fakeHeritageEntityList)
            startKoin { modules(repositoryModule) }
            declareMock<Repository> {
                runBlocking {
                    doAnswer { fakeSuccessResource }.whenever(this@declareMock).fetchHeritagesList()
                }
            }

            val observer: Observer<HeritageResponse> = mock()

            val sut =
                MainViewModel(
                    coroutinesIoDispatcher = TestCoroutineDispatcher(),
                    repository = repositoryMock
                )

            sut.worldHeritagesLiveData.observeForever(observer)
            sut.worldHeritagesLiveData

            val captor = argumentCaptor<HeritageResponse>()
            captor.run {
                verify(observer, times(2)).onChanged(capture())
                assertEquals(fakeSuccessResource.data, lastValue.data)
            }
        }

    @Test
    fun `given an erroneous list of heritages it should first emit loading and then error`() =
        runBlockingTest {
            startKoin { modules(repositoryModule) }
            val errorMessage = "Error"
            declareMock<Repository> {
                runBlocking {
                    doAnswer { Resource.Error<List<Heritage?>?>(errorMessage) }.whenever(this@declareMock)
                        .fetchHeritagesList()
                }
            }

            val observer: Observer<HeritageResponse> = mock()

            val sut =
                MainViewModel(
                    coroutinesIoDispatcher = TestCoroutineDispatcher(),
                    repository = repositoryMock
                )

            sut.worldHeritagesLiveData.observeForever(observer)
            sut.worldHeritagesLiveData
            val captor = argumentCaptor<HeritageResponse>()
            captor.run {
                verify(observer, times(2)).onChanged(capture())
                assertEquals(errorMessage, lastValue.message)
            }
        }
}