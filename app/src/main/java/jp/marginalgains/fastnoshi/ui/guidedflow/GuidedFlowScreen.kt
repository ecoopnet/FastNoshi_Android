package jp.marginalgains.fastnoshi.ui.guidedflow

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import jp.marginalgains.fastnoshi.domain.model.GuidedFlowStep
import jp.marginalgains.fastnoshi.ui.components.NoshiChoiceButton
import jp.marginalgains.fastnoshi.ui.components.NoshiTopBar
import jp.marginalgains.fastnoshi.ui.theme.NoshiSpacing

private const val TOTAL_STEPS = 4

@Composable
fun GuidedFlowScreen(
    onNavigateToTextInput: (templateId: String, omoteGaki: String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: GuidedFlowViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.navigateToTextInput) {
        uiState.navigateToTextInput?.let { nav ->
            onNavigateToTextInput(nav.templateId, nav.omoteGaki)
            viewModel.onNavigationConsumed()
        }
    }

    Scaffold(
        topBar = {
            NoshiTopBar(
                title = "のし紙を作る",
                onBackClick = if (uiState.canGoBack) {
                    { viewModel.onGoBack() }
                } else {
                    onBackClick
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // プログレスバー + ステップテキスト
            ProgressSection(
                currentStep = uiState.stepIndex + 1,
                totalSteps = TOTAL_STEPS
            )

            // メインコンテンツ
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        horizontal = NoshiSpacing.spacingLG,
                        vertical = NoshiSpacing.spacingMD
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(NoshiSpacing.spacingMD))
                AnimatedContent(
                    targetState = uiState.currentStep,
                    label = "step_transition"
                ) { step ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingXL)
                    ) {
                        // 質問アイコン
                        androidx.compose.material3.Icon(
                            imageVector = stepIcon(step),
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )

                        Text(
                            text = uiState.questionText,
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(NoshiSpacing.spacingSM))

                        if (step == GuidedFlowStep.OMOTE_GAKI_SELECTION) {
                            OmoteGakiSelection(
                                candidates = uiState.omoteGakiCandidates,
                                onSelected = viewModel::onOmoteGakiSelected
                            )
                        } else {
                            ChoicesList(
                                choices = uiState.choices,
                                onSelected = viewModel::onChoiceSelected
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProgressSection(currentStep: Int, totalSteps: Int) {
    Column(modifier = Modifier.fillMaxWidth()) {
        LinearProgressIndicator(
            progress = { currentStep.toFloat() / totalSteps },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
            drawStopIndicator = {}
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = NoshiSpacing.spacingLG,
                    vertical = NoshiSpacing.spacingSM
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ステップ $currentStep / $totalSteps",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

private fun stepIcon(step: GuidedFlowStep) = when (step) {
    GuidedFlowStep.PURPOSE -> Icons.Default.CardGiftcard
    GuidedFlowStep.CELEBRATION_REPEAT -> Icons.Default.Repeat
    GuidedFlowStep.MARRIAGE_CHECK -> Icons.Default.Favorite
    GuidedFlowStep.CONDOLENCE_TYPE -> Icons.Default.Eco
    GuidedFlowStep.VISIT_TYPE -> Icons.Default.MedicalInformation
    GuidedFlowStep.OMOTE_GAKI_SELECTION -> Icons.Default.Description
}

@Composable
private fun ChoicesList(
    choices: List<jp.marginalgains.fastnoshi.domain.model.FlowChoice>,
    onSelected: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingMD)) {
        choices.forEachIndexed { index, choice ->
            NoshiChoiceButton(
                text = choice.label,
                onClick = { onSelected(index) }
            )
        }
    }
}

@Composable
private fun OmoteGakiSelection(candidates: List<String>, onSelected: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingMD)) {
        candidates.forEach { candidate ->
            NoshiChoiceButton(
                text = candidate,
                onClick = { onSelected(candidate) }
            )
        }
    }
}
