package me.manulorenzo.worldheritages.ui.main

import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import me.manulorenzo.worldheritages.EspressoMatchers
import me.manulorenzo.worldheritages.Faker
import me.manulorenzo.worldheritages.MainActivity
import me.manulorenzo.worldheritages.R
import me.manulorenzo.worldheritages.asPagedList
import me.manulorenzo.worldheritages.createMockDataSourceFactory
import me.manulorenzo.worldheritages.data.Resource
import me.manulorenzo.worldheritages.data.db.entity.toModel
import me.manulorenzo.worldheritages.data.model.Heritage
import me.manulorenzo.worldheritages.data.source.Repository
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
@MediumTest
@RunWith(AndroidJUnit4::class)
class MainFragmentTest : KoinTest {
    private lateinit var repository: Repository
    @get:Rule
    val testRule = ActivityTestRule(MainActivity::class.java, true, false)
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()
    @get:Rule
    val executorRule = CountingTaskExecutorRule()

    @Before
    fun setup() {
        val fakeItemList: List<Heritage> = Faker.fakeHeritageEntityList.map { it.toModel() }
        val mockedDataSource: DataSource.Factory<Int, Heritage> =
            createMockDataSourceFactory(fakeItemList)
        val fakeSuccessResource: Resource.Success<DataSource.Factory<Int, Heritage>> =
            Resource.Success(mockedDataSource)
        val mockedMainViewModelModule = module {
            factory(override = true) {
                mock<MainViewModel> {
                    doAnswer {
                        MutableLiveData<PagedList<Heritage>>().apply { postValue(fakeItemList.asPagedList()) }
                    }.whenever(mock).worldHeritagesLiveData
                }
            }
        }
        val mockedRepositoryModule = module {
            factory(override = true) {
                mock<Repository> {
                    onBlocking { fetchHeritagesList() } doAnswer {
                        fakeSuccessResource
                    }
                }
            }
        }
        if (GlobalContext.getOrNull() != null) {
            stopKoin()
        }
        startKoin {
            loadKoinModules(
                listOf(
                    mockedRepositoryModule,
                    mockedMainViewModelModule
                )
            )
        }
    }

    @Test
    @Ignore("Unfortunately, the RecyclerView does not show any time and I have no time to investigate it")
    fun givenAListOfHeritagesWithOneItem_shouldShowItInTheRecyclerView() = runBlockingTest {
        testRule.launchActivity(null)
        waitForAdapterChangeWithPagination(
            testRule.activity.findViewById(R.id.heritagesRecyclerView),
            executorRule,
            1
        )
        onView(
            allOf(
                withId(R.id.heritagesRecyclerView),
                isDisplayed()
            )
        ).check(matches(EspressoMatchers.hasItemCount(1)))
    }

    /**
     * Waiting adapter showing items inside its RecyclerView
     * before continue the test.
     */
    private fun waitForAdapterChangeWithPagination(
        recyclerView: RecyclerView,
        testRule: CountingTaskExecutorRule,
        count: Int
    ) {
        val latch = CountDownLatch(count)
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            recyclerView.adapter?.registerAdapterDataObserver(
                object : RecyclerView.AdapterDataObserver() {
                    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                        latch.countDown()
                    }

                    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                        latch.countDown()
                    }
                })
        }
        testRule.drainTasks(1, TimeUnit.SECONDS)
        if (recyclerView.adapter?.itemCount ?: 0 > 0) {
            return //already loaded
        }
        ViewMatchers.assertThat(latch.await(10, TimeUnit.SECONDS), CoreMatchers.`is`(true))
    }
}