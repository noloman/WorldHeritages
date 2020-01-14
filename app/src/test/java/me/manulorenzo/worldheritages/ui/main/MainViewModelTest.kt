package me.manulorenzo.worldheritages.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import me.manulorenzo.worldheritages.CoroutinesTestRule
import me.manulorenzo.worldheritages.Faker
import me.manulorenzo.worldheritages.MockLimitDataSource
import me.manulorenzo.worldheritages.createMockDataSourceFactory
import me.manulorenzo.worldheritages.data.Resource
import me.manulorenzo.worldheritages.data.db.entity.toModel
import me.manulorenzo.worldheritages.data.di.repositoryModule
import me.manulorenzo.worldheritages.data.model.Heritage
import me.manulorenzo.worldheritages.data.source.Repository
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
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
    fun `given a fake of a list of heritages it should emit a PagedList whose dataSource is the fake list`() =
        runBlockingTest {
            val fakeItemList: List<Heritage> = Faker.fakeHeritageEntityList.map { it.toModel() }
            val mockedDataSource = createMockDataSourceFactory(fakeItemList)
            val fakeSuccessResource: Resource.Success<DataSource.Factory<Int, Heritage>> =
                Resource.Success(mockedDataSource)
            startKoin { modules(repositoryModule) }
            declareMock<Repository> {
                runBlocking {
                    whenever(this@declareMock.fetchHeritagesList()).doReturn(fakeSuccessResource)
                }
            }

            val observer: Observer<PagedList<Heritage>?> = mock()

            val sut = MainViewModel(repository = repositoryMock)

            sut.worldHeritagesLiveData?.observeForever(observer)
            sut.worldHeritagesLiveData

            val captor = argumentCaptor<PagedList<Heritage>>()
            captor.run {
                verify(observer).onChanged(capture())
                assertTrue(lastValue.dataSource is MockLimitDataSource)
                assertTrue((lastValue.dataSource as MockLimitDataSource).itemList == fakeItemList)
            }
        }

    @Test
    fun `given an erroneous response from the repository errorPagedList LiveData should return true`() =
        runBlockingTest {
            startKoin { modules(repositoryModule) }
            val errorMessage = "Error"
            declareMock<Repository> {
                runBlocking {
                    doAnswer { Resource.Error<String>(errorMessage) }.whenever(this@declareMock)
                        .fetchHeritagesList()
                }
            }

            val errorObserver: Observer<Boolean> = mock()

            val sut =
                MainViewModel(
                    repository = repositoryMock
                )

            sut.errorPagedList.observeForever(errorObserver)
            sut.worldHeritagesLiveData
            val captor = argumentCaptor<Boolean>()
            captor.run {
                verify(errorObserver).onChanged(capture())
                assertNotNull(sut.errorPagedList.value)
                assertTrue(sut.errorPagedList.value!!)
            }
            assertNull(sut.worldHeritagesLiveData)
        }

    @Test
    fun `given an erroneous list of heritages it should emit a null value`() =
        runBlockingTest {
            startKoin { modules(repositoryModule) }
            val errorMessage = "Error"
            declareMock<Repository> {
                runBlocking {
                    doAnswer { Resource.Error<String>(errorMessage) }.whenever(this@declareMock)
                        .fetchHeritagesList()
                }
            }

            val observer: Observer<PagedList<Heritage>?> = mock()

            val sut =
                MainViewModel(
                    repository = repositoryMock
                )

            sut.worldHeritagesLiveData?.observeForever(observer)
            sut.worldHeritagesLiveData
            val captor = argumentCaptor<PagedList<Heritage>>()
            captor.run {
                verify(observer, never()).onChanged(capture())
            }
            assertNull(sut.worldHeritagesLiveData)
        }
}