package com.example.thirukuralapp.viewmodel

import android.content.Context
import com.example.thirukuralapp.ext.BaseMockkTest
import com.example.thirukuralapp.ext.InstantExecutorExtension
import com.example.thirukuralapp.model.ThirukuralResponse
import com.example.thirukuralapp.repository.ThirukuralRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

@DisplayName("ThirukuralViewmodel")
@ExtendWith(InstantExecutorExtension::class)
@ExperimentalCoroutinesApi
class ThirukuralViewmodelTest :BaseMockkTest(){
    private lateinit var viewModel: ThirukuralViewmodel
    private lateinit var mockRepository: ThirukuralRepository
    private lateinit var mockContext: Context

    @BeforeEach
    fun setUp() {
        mockRepository = mockk()
        mockContext = mockk()
        viewModel = mockk()

        every { viewModel.thisContext } returns mockContext

        startKoin {
            modules(
                    module {
                        single { mockRepository }
                    }
            )
        }
        viewModel = spyk(ThirukuralViewmodel())
    }

    @AfterEach
    override fun afterEach() {
        super.afterEach()
        stopKoin()
    }

    @Nested
    @DisplayName("getThirukural")
    inner class GetThirukural {
        private lateinit var thiruKural: ThirukuralResponse
        @BeforeEach
        fun beforeEach() {
            coEvery { mockRepository.getThirukural(any()) } returns thiruKural
        }

        @Test
        fun `should call repository getSchools`() {
            viewModel.getThirukural(1).observeForever {}
            coVerify { mockRepository.getThirukural(1) }
        }

        @Test
        fun `should emit schools on success`() {
            val liveData = viewModel.getThirukural(1)
            liveData.observeForever {}

            val result = liveData.value
            Assertions.assertNotNull(result)

            if (result != null) {
                Assertions.assertTrue(result.isSuccess)
                Assertions.assertEquals(thiruKural, result.getOrNull())
            }
        }

        @Test
        fun `should emit error on failure`() {
            val fakeError = Error("boo")
            coEvery { mockRepository.getThirukural(any()) } throws fakeError

            val liveData = viewModel.getThirukural(1)
            liveData.observeForever {}

            val result = liveData.value
            Assertions.assertNotNull(result)

            if (result != null) {
                Assertions.assertTrue(result.isFailure)
                Assertions.assertEquals(fakeError, result.exceptionOrNull())
            }
        }
    }
}