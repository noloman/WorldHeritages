package me.manulorenzo.worldheritages.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import me.manulorenzo.worldheritages.Faker
import me.manulorenzo.worldheritages.data.db.dao.HeritageDao
import me.manulorenzo.worldheritages.data.model.Heritage
import me.manulorenzo.worldheritages.data.source.Repository
import me.manulorenzo.worldheritages.tryCast
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var sut: Repository
    private val mockParserManager: ParserManager = mock()
    private val mockHeritageDao: HeritageDao = mock()

    @Before
    fun setUp() {
        sut = Repository(
            parserManager = mockParserManager,
            heritageDao = mockHeritageDao
        )
    }

    /**
     * Unfortunately running this test with [runBlockingTest] results in
     * [Exception in thread "main" java.lang.IllegalStateException: This job has not completed yet]
     * More information: https://github.com/Kotlin/kotlinx.coroutines/issues/1204
     */
    @Test
    fun `given a list of heritages, it should fetch them wrapped in a Result#Success`() =
        runBlocking {
            whenever(mockHeritageDao.getHeritageEntityDataSource()).thenReturn(Faker.fakeHeritageEntityList)
            val heritageList = sut.fetchHeritagesList()
            tryCast<Resource.Success<List<Heritage?>?>>(heritageList) {
                assertTrue(heritageList is Resource.Success<List<Heritage?>?>)
            }
            tryCast<List<Heritage?>>(heritageList.data) {
                assertTrue(heritageList.data is List<Heritage?>)
            }
            assertTrue(heritageList.data.size == 1)
        }

    @Test
    fun `given a wrong list of heritages, it should fetch them wrapped in a Result#Error`() {
        runBlocking {
            whenever(mockHeritageDao.getHeritageEntityDataSource()).doThrow(JsonDataException::class)
            val heritageList = sut.fetchHeritagesList()
            tryCast<Resource.Error<List<Heritage?>?>>(heritageList) {
                assertTrue(heritageList is Resource.Error<List<Heritage?>?>)
            }
            tryCast<List<Heritage?>>(heritageList.data) {
                assertTrue(heritageList.data is List<Heritage?>)
            }
            assertNull(heritageList.data)
        }
    }
}