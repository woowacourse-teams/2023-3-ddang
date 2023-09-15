package com.ddangddangddang.data.study.coroutine

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test
import java.util.concurrent.TimeoutException
import kotlin.coroutines.resume

internal class CoroutineTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `5초 이내에 코루틴을 재개하지 않으면 무한히 기다리다가 시간초과가 발생한다`() = runTest {
        // given

        // when

        // then
        assertThrows(TimeoutException::class.java) { runBlocking { doNotResume(5000L) } }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `5초 이내에 코루틴을 재개하면 success 문자가 반환된다`() = runTest {
        // given

        // when
        val actual = doResume(3000L, 5000L)

        // then
        assertThat(actual).isEqualTo("success")
    }

    private suspend fun doNotResume(timeOut: Long) {
        val startTime = System.currentTimeMillis()
        var currentTime = startTime
        return suspendCancellableCoroutine {
            while (true) {
                currentTime = System.currentTimeMillis()
                if (currentTime - startTime > timeOut) {
                    throw TimeoutException()
                }
            }
        }
    }

    private suspend fun doResume(resumeTime: Long, timeOut: Long): String {
        val startTime = System.currentTimeMillis()
        var currentTime = startTime
        return suspendCancellableCoroutine { continuation ->
            while (true) {
                currentTime = System.currentTimeMillis()
                if (currentTime - startTime in (resumeTime..timeOut)) {
                    return@suspendCancellableCoroutine continuation.resume("success")
                }

                if (currentTime - startTime > timeOut) {
                    throw TimeoutException()
                }
            }
        }
    }
}
