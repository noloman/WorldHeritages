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
                Resource.Success(
                    listOf(
                        Heritage(
                            id = "3",
                            year = 1978,
                            target = "DEU",
                            name = "Aachen Cathedral ",
                            type = "Cultural",
                            region = "EUR",
                            regionLong = "Europe and North America",
                            lat = 50.77444444444444,
                            lng = 6.084444444444444,
                            page = "http=//whc.unesco.org/en/list/3",
                            image = "http://whc.unesco.org//uploads/thumbs/site_0003_0001-750-0-20131014170237.jpg",
                            imageAuthor = "Aachen Cathedral © Mario Santana ",
                            shortInfo = "Aachen Cathedral \n\nConstruction of this palatine chapel, with its octagonal basilica and cupola, began c. 790–800 under the Emperor Charlemagne. Originally inspired by the churches of the Eastern part of the Holy Roman Empire, it was splendidly enlarged in the Middle Ages. ",
                            longInfo = "With its columns of Greek and Italian marble, its bronze doors, the largest mosaic of its dome (now destroyed), the Palatine Chapel of Aachen has, from its inception, been perceived as an exceptional artistic creation. It was the first vaulted structure to be constructed north of the Alps since antiquity. It remained, during the Carolingian Renaissance and even at the beginning of the medieval period, one of the prototypes of religious architecture which led to copies or imitations (Mettlach, Nijmegen). It is an excellent and distinctive example of the family of aularian chapels based on a central plan with tribunes.\n\nThe construction of the chapel of the Emperor at Aachen symbolized the unification of the west and its spiritual and political revival under the aegis of Charlemagne. In 814, Charlemagne was buried here, and throughout the Middle Ages until 1531 the Germanic emperors continued to be crowned here. The collection of the treasury of the cathedral is of incalculable archaeological, aesthetic, and historic interest.\n\nThe most important historical epoch of Aachen started with the takeover of the government by Charlemagne in AD 768. The imperial palace by the hot springs soon became his permanent residence and so developed into a spiritual and cultural centre. Two hundred years later he was canonized, which resulted in a flow of pilgrims wishing to see Charlemagne's tomb and the relics he gathered during his life. The town's ties with Charlemagne are reflected in numerous architectural heirlooms and memorials in the townscape.\n\nWhen he began work on his Palatine Chapel in 786, Charlemagne's dream was to create a 'new Rome'. The core of Aachen Cathedral at the time of its construction was the largest dome north of the Alps. Its fascinating architecture, with classical, Byzantine and Germanic-Franconian elements, is the essence of a monumental building of the greatest importance. For 600 years, from 936 to 1531, Aachen Cathedral was the coronation church for thirty German kings, and even today it retains much of the glamour of its historic past.\n\nIts present form has evolved over the course of more than a millennium. Two parts of the original complex have survived: the Coronation Hall (Aula Regia), which is currently located in the Town Hall, built in the 14th century, and the Palatine Chapel, around which the cathedral would later be built.\n\nThe Palatine Chapel, constructed about 790-800, is based on an octagonal ground plan, which is ringed by an aisle, surmounted by tribunes and roofed with a dome; the chapel itself is easily distinguished from later additions by its distinctive structure. An atrium on the western side led, through a portico, to the imperial apartments. The Gothic choir and a series of chapels that were added throughout the Middle Ages created the composite array of features that characterized the cathedral.\n\nThe interior is punctuated on the lower storey by round arches set upon eight stout cruciform pillars, and on the upper storey by the matroneum, a gallery for women. The populace was admitted in the lower part of the chapel; the Emperor sat up high, facing the altar, on the stone throne upon which the kings of Germany would be crowned. The high dome gathers light from eight open-arched windows above the drum; it was originally entirely covered with a great mosaic depicting Christ Enthroned, in purple robes and surrounded by the Elders of the Apocalypse. The present-day mosaic date back to 1870-73. The interior of the chapel is embellished by coloured marbles that Charlemagne probably ordered to be brought from Rome and Ravenna. Despite the subsequent additions, the Palatine Chapel constitutes a unitary nucleus.\n\nThe Cathedral Treasury in Aachen is regarded as one of the most important ecclesiastical treasuries in northern Europe. The crypt of the cathedral contains the cross of Lothar (990), made from gold and inlaid with precious stones, the dark-blue velvet chasuble with embroidered pearls, a reliquary-bust of Charlemagne made from silver and gold, and a marble sarcophagus decorated with a relief of the Abduction of Proserpine, which once contained the body of Charlemagne."
                        )
                    )
                )
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
}