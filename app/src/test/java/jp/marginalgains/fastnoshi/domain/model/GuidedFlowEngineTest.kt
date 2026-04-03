package jp.marginalgains.fastnoshi.domain.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class GuidedFlowEngineTest {

    private lateinit var engine: GuidedFlowEngine

    @BeforeEach
    fun setUp() {
        engine = GuidedFlowEngine()
    }

    @Test
    fun `初期状態はPURPOSE`() {
        assertEquals(GuidedFlowStep.PURPOSE, engine.currentStep)
    }

    @Test
    fun `初期状態ではtemplateIDとcandidatesはnull`() {
        assertNull(engine.selectedTemplateId)
        assertTrue(engine.omoteGakiCandidates.isEmpty())
    }

    @Test
    fun `初期状態ではgoBackできない`() {
        assertFalse(engine.canGoBack)
    }

    @Test
    fun `PURPOSE の質問文が正しい`() {
        assertEquals("どのようなご用途ですか？", engine.questionText)
    }

    @Test
    fun `PURPOSE の選択肢は3つ`() {
        val choices = engine.availableChoices
        assertEquals(3, choices.size)
        assertEquals("お祝い", choices[0].label)
        assertEquals("お見舞い・快気", choices[1].label)
        assertEquals("お悔やみ", choices[2].label)
    }

    @Nested
    inner class CelebrationFlow {
        @Test
        fun `お祝い選択でCELEBRATION_REPEATへ遷移`() {
            engine.select(0) // お祝い
            assertEquals(GuidedFlowStep.CELEBRATION_REPEAT, engine.currentStep)
            assertEquals("何度あってもよいお祝いですか？", engine.questionText)
        }

        @Test
        fun `繰り返すお祝い - はい → 蝶結びで表書き選択へ`() {
            engine.select(0) // お祝い
            engine.select(0) // はい
            assertEquals(GuidedFlowStep.OMOTE_GAKI_SELECTION, engine.currentStep)
            assertEquals("05_cho_red_on", engine.selectedTemplateId)
            assertTrue(engine.omoteGakiCandidates.containsAll(listOf("御祝", "御出産祝", "御入学祝")))
        }

        @Test
        fun `繰り返すお祝い - いいえ → MARRIAGE_CHECKへ`() {
            engine.select(0) // お祝い
            engine.select(1) // いいえ
            assertEquals(GuidedFlowStep.MARRIAGE_CHECK, engine.currentStep)
            assertEquals("結婚ですか？", engine.questionText)
        }

        @Test
        fun `結婚 - はい → 10本結びで表書き選択へ`() {
            engine.select(0) // お祝い
            engine.select(1) // いいえ
            engine.select(0) // はい（結婚）
            assertEquals(GuidedFlowStep.OMOTE_GAKI_SELECTION, engine.currentStep)
            assertEquals("10_musu_red_on", engine.selectedTemplateId)
            assertTrue(engine.omoteGakiCandidates.containsAll(listOf("御結婚祝", "寿")))
        }

        @Test
        fun `結婚 - いいえ → 結び切りで表書き選択へ`() {
            engine.select(0) // お祝い
            engine.select(1) // いいえ
            engine.select(1) // いいえ（結婚でない）
            assertEquals(GuidedFlowStep.OMOTE_GAKI_SELECTION, engine.currentStep)
            assertEquals("05_musu_red_off", engine.selectedTemplateId)
            assertTrue(engine.omoteGakiCandidates.containsAll(listOf("御祝", "御礼")))
        }
    }

    @Nested
    inner class CondolenceFlow {
        @Test
        fun `お悔やみ選択でCONDOLENCE_TYPEへ遷移`() {
            engine.select(2) // お悔やみ
            assertEquals(GuidedFlowStep.CONDOLENCE_TYPE, engine.currentStep)
            assertEquals("通夜・葬儀ですか？法要ですか？", engine.questionText)
        }

        @Test
        fun `通夜・葬儀 → 黒白結び切りで表書き選択へ`() {
            engine.select(2) // お悔やみ
            engine.select(0) // 通夜・葬儀
            assertEquals(GuidedFlowStep.OMOTE_GAKI_SELECTION, engine.currentStep)
            assertEquals("05_musu_black_off", engine.selectedTemplateId)
            assertTrue(engine.omoteGakiCandidates.containsAll(listOf("御霊前", "御香典")))
        }

        @Test
        fun `法要 → 黒白結び切りで表書き選択へ`() {
            engine.select(2) // お悔やみ
            engine.select(1) // 法要
            assertEquals(GuidedFlowStep.OMOTE_GAKI_SELECTION, engine.currentStep)
            assertEquals("05_musu_black_off", engine.selectedTemplateId)
            assertTrue(engine.omoteGakiCandidates.containsAll(listOf("御仏前", "御供")))
        }
    }

    @Nested
    inner class VisitFlow {
        @Test
        fun `お見舞い選択でVISIT_TYPEへ遷移`() {
            engine.select(1) // お見舞い・快気
            assertEquals(GuidedFlowStep.VISIT_TYPE, engine.currentStep)
            assertEquals("現在の状況は？", engine.questionText)
        }

        @Test
        fun `入院中 → 結び切りで御見舞`() {
            engine.select(1) // お見舞い・快気
            engine.select(0) // 入院中
            assertEquals(GuidedFlowStep.OMOTE_GAKI_SELECTION, engine.currentStep)
            assertEquals("05_musu_red_off", engine.selectedTemplateId)
            assertEquals(listOf("御見舞"), engine.omoteGakiCandidates)
        }

        @Test
        fun `退院（完治） → 結び切りで快気祝`() {
            engine.select(1) // お見舞い・快気
            engine.select(1) // 退院（完治）
            assertEquals(GuidedFlowStep.OMOTE_GAKI_SELECTION, engine.currentStep)
            assertEquals("05_musu_red_off", engine.selectedTemplateId)
            assertEquals(listOf("快気祝"), engine.omoteGakiCandidates)
        }

        @Test
        fun `退院（療養中） → 結び切りで快気内祝`() {
            engine.select(1) // お見舞い・快気
            engine.select(2) // 退院（療養中）
            assertEquals(GuidedFlowStep.OMOTE_GAKI_SELECTION, engine.currentStep)
            assertEquals("05_musu_red_off", engine.selectedTemplateId)
            assertEquals(listOf("快気内祝"), engine.omoteGakiCandidates)
        }
    }

    @Nested
    inner class GoBackOperation {
        @Test
        fun `1ステップ進んだ後goBackで戻れる`() {
            engine.select(0) // お祝い
            assertTrue(engine.canGoBack)
            engine.goBack()
            assertEquals(GuidedFlowStep.PURPOSE, engine.currentStep)
            assertFalse(engine.canGoBack)
        }

        @Test
        fun `表書き選択から戻ると候補とテンプレートがクリアされる`() {
            engine.select(0) // お祝い
            engine.select(0) // はい → OMOTE_GAKI_SELECTION
            assertEquals("05_cho_red_on", engine.selectedTemplateId)
            assertTrue(engine.omoteGakiCandidates.isNotEmpty())

            engine.goBack()
            assertEquals(GuidedFlowStep.CELEBRATION_REPEAT, engine.currentStep)
            assertNull(engine.selectedTemplateId)
            assertTrue(engine.omoteGakiCandidates.isEmpty())
        }

        @Test
        fun `複数ステップ戻れる`() {
            engine.select(0) // お祝い
            engine.select(1) // いいえ
            engine.select(0) // はい（結婚）→ OMOTE_GAKI_SELECTION

            engine.goBack() // → MARRIAGE_CHECK
            assertEquals(GuidedFlowStep.MARRIAGE_CHECK, engine.currentStep)

            engine.goBack() // → CELEBRATION_REPEAT
            assertEquals(GuidedFlowStep.CELEBRATION_REPEAT, engine.currentStep)

            engine.goBack() // → PURPOSE
            assertEquals(GuidedFlowStep.PURPOSE, engine.currentStep)
            assertFalse(engine.canGoBack)
        }
    }

    @Nested
    inner class ResetOperation {
        @Test
        fun `resetで初期状態に戻る`() {
            engine.select(0) // お祝い
            engine.select(0) // はい
            engine.reset()

            assertEquals(GuidedFlowStep.PURPOSE, engine.currentStep)
            assertNull(engine.selectedTemplateId)
            assertTrue(engine.omoteGakiCandidates.isEmpty())
            assertFalse(engine.canGoBack)
        }
    }

    @Test
    fun `OMOTE_GAKI_SELECTION の質問文が正しい`() {
        engine.select(0) // お祝い
        engine.select(0) // はい
        assertEquals("おすすめの表書きはこちらになります。選んでください", engine.questionText)
    }
}
