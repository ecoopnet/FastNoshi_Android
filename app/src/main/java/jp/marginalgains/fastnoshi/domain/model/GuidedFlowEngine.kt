package jp.marginalgains.fastnoshi.domain.model

data class FlowChoice(
    val label: String,
    val nextStep: GuidedFlowStep,
    val templateId: String? = null,
    val candidates: List<String> = emptyList()
)

class GuidedFlowEngine {

    var currentStep: GuidedFlowStep = GuidedFlowStep.PURPOSE
        private set

    var selectedTemplateId: String? = null
        private set

    var omoteGakiCandidates: List<String> = emptyList()
        private set

    private val history = mutableListOf<GuidedFlowStep>()

    val canGoBack: Boolean get() = history.isNotEmpty()

    val questionText: String
        get() = when (currentStep) {
            GuidedFlowStep.PURPOSE -> "どのようなご用途ですか？"
            GuidedFlowStep.CELEBRATION_REPEAT -> "何度あってもよいお祝いですか？"
            GuidedFlowStep.MARRIAGE_CHECK -> "結婚ですか？"
            GuidedFlowStep.CONDOLENCE_TYPE -> "通夜・葬儀ですか？法要ですか？"
            GuidedFlowStep.VISIT_TYPE -> "現在の状況は？"
            GuidedFlowStep.OMOTE_GAKI_SELECTION -> "おすすめの表書きはこちらになります。選んでください"
        }

    val availableChoices: List<FlowChoice>
        get() = when (currentStep) {
            GuidedFlowStep.PURPOSE -> listOf(
                FlowChoice("お祝い", GuidedFlowStep.CELEBRATION_REPEAT),
                FlowChoice("お見舞い・快気", GuidedFlowStep.VISIT_TYPE),
                FlowChoice("お悔やみ", GuidedFlowStep.CONDOLENCE_TYPE)
            )

            GuidedFlowStep.CELEBRATION_REPEAT -> listOf(
                FlowChoice(
                    "はい",
                    GuidedFlowStep.OMOTE_GAKI_SELECTION,
                    templateId = "05_cho_red_on",
                    candidates = listOf("御祝", "御出産祝", "御入学祝", "御新築祝", "御開店祝", "内祝")
                ),
                FlowChoice("いいえ", GuidedFlowStep.MARRIAGE_CHECK)
            )

            GuidedFlowStep.MARRIAGE_CHECK -> listOf(
                FlowChoice(
                    "はい",
                    GuidedFlowStep.OMOTE_GAKI_SELECTION,
                    templateId = "10_musu_red_on",
                    candidates = listOf("御結婚祝", "寿")
                ),
                FlowChoice(
                    "いいえ",
                    GuidedFlowStep.OMOTE_GAKI_SELECTION,
                    templateId = "05_musu_red_off",
                    candidates = listOf("御祝", "御礼")
                )
            )

            GuidedFlowStep.CONDOLENCE_TYPE -> listOf(
                FlowChoice(
                    "通夜・葬儀",
                    GuidedFlowStep.OMOTE_GAKI_SELECTION,
                    templateId = "05_musu_black_off",
                    candidates = listOf("御霊前", "御香典")
                ),
                FlowChoice(
                    "法要",
                    GuidedFlowStep.OMOTE_GAKI_SELECTION,
                    templateId = "05_musu_black_off",
                    candidates = listOf("御仏前", "御供")
                )
            )

            GuidedFlowStep.VISIT_TYPE -> listOf(
                FlowChoice(
                    "入院中",
                    GuidedFlowStep.OMOTE_GAKI_SELECTION,
                    templateId = "05_musu_red_off",
                    candidates = listOf("御見舞")
                ),
                FlowChoice(
                    "退院（完治）",
                    GuidedFlowStep.OMOTE_GAKI_SELECTION,
                    templateId = "05_musu_red_off",
                    candidates = listOf("快気祝")
                ),
                FlowChoice(
                    "退院（療養中）",
                    GuidedFlowStep.OMOTE_GAKI_SELECTION,
                    templateId = "05_musu_red_off",
                    candidates = listOf("快気内祝")
                )
            )

            GuidedFlowStep.OMOTE_GAKI_SELECTION -> emptyList()
        }

    fun select(choiceIndex: Int) {
        val choices = availableChoices
        require(choiceIndex in choices.indices)
        val choice = choices[choiceIndex]

        history.add(currentStep)
        currentStep = choice.nextStep

        if (choice.templateId != null) {
            selectedTemplateId = choice.templateId
            omoteGakiCandidates = choice.candidates
        }
    }

    fun goBack() {
        check(canGoBack)
        currentStep = history.removeLast()
        selectedTemplateId = null
        omoteGakiCandidates = emptyList()
    }

    fun reset() {
        currentStep = GuidedFlowStep.PURPOSE
        selectedTemplateId = null
        omoteGakiCandidates = emptyList()
        history.clear()
    }
}
