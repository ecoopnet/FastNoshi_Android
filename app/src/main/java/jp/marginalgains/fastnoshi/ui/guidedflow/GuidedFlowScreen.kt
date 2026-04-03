package jp.marginalgains.fastnoshi.ui.guidedflow

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import jp.marginalgains.fastnoshi.domain.model.GuidedFlowStep
import jp.marginalgains.fastnoshi.ui.components.NoshiChoiceButton
import jp.marginalgains.fastnoshi.ui.components.NoshiTopBar
import jp.marginalgains.fastnoshi.ui.components.StepIndicator

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
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StepIndicator(
                totalSteps = TOTAL_STEPS,
                currentStep = uiState.stepIndex
            )
            Spacer(modifier = Modifier.height(32.dp))
            AnimatedContent(
                targetState = uiState.currentStep,
                label = "step_transition"
            ) { step ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = uiState.questionText,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
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

@Composable
private fun ChoicesList(
    choices: List<jp.marginalgains.fastnoshi.domain.model.FlowChoice>,
    onSelected: (Int) -> Unit
) {
    choices.forEachIndexed { index, choice ->
        NoshiChoiceButton(
            text = choice.label,
            onClick = { onSelected(index) }
        )
    }
}

@Composable
private fun OmoteGakiSelection(candidates: List<String>, onSelected: (String) -> Unit) {
    candidates.forEach { candidate ->
        NoshiChoiceButton(
            text = candidate,
            onClick = { onSelected(candidate) }
        )
    }
}
