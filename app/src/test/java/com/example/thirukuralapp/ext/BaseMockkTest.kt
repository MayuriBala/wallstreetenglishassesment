package com.example.thirukuralapp.ext

import io.mockk.clearAllMocks
import org.junit.jupiter.api.AfterEach

open class BaseMockkTest {

    @AfterEach
    open fun afterEach() {
        clearAllMocks()
    }
}