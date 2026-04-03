package jp.marginalgains.fastnoshi.ui.settings

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import jp.marginalgains.fastnoshi.data.repository.PreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var preferencesRepository: PreferencesRepository
    private lateinit var viewModel: SettingsViewModel

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        preferencesRepository = mockk(relaxed = true)
        every { preferencesRepository.defaultFontId } returns flowOf("mincho")
        every { preferencesRepository.defaultPaperSize } returns flowOf("A4")
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `初期状態でDataStoreの値を読み込む`() = runTest {
        viewModel = SettingsViewModel(preferencesRepository)

        viewModel.uiState.test {
            val initial = awaitItem()
            assertEquals("mincho", initial.defaultFontId)
            assertEquals("A4", initial.defaultPaperSize)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `フォントを変更するとDataStoreに保存される`() = runTest {
        viewModel = SettingsViewModel(preferencesRepository)

        viewModel.updateDefaultFont("gothic")
        advanceUntilIdle()

        coVerify { preferencesRepository.setDefaultFontId("gothic") }
    }

    @Test
    fun `用紙サイズを変更するとDataStoreに保存される`() = runTest {
        viewModel = SettingsViewModel(preferencesRepository)

        viewModel.updateDefaultPaperSize("B4")
        advanceUntilIdle()

        coVerify { preferencesRepository.setDefaultPaperSize("B4") }
    }

    @Test
    fun `DataStoreの値変更がUIに反映される`() = runTest {
        every { preferencesRepository.defaultFontId } returns flowOf("gothic")
        every { preferencesRepository.defaultPaperSize } returns flowOf("B4")
        viewModel = SettingsViewModel(preferencesRepository)

        viewModel.uiState.test {
            // initialValue(デフォルト)が先に来る場合があるのでskip
            var state = awaitItem()
            if (state.defaultFontId == "mincho") {
                state = awaitItem()
            }
            assertEquals("gothic", state.defaultFontId)
            assertEquals("B4", state.defaultPaperSize)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
