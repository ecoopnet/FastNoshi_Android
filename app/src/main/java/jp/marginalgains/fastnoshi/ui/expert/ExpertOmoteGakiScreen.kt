package jp.marginalgains.fastnoshi.ui.expert

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import jp.marginalgains.fastnoshi.domain.model.NoshiTemplate
import jp.marginalgains.fastnoshi.ui.components.NoshiChoiceButton
import jp.marginalgains.fastnoshi.ui.components.NoshiPrimaryButton
import jp.marginalgains.fastnoshi.ui.components.NoshiTopBar
import jp.marginalgains.fastnoshi.ui.theme.NoshiSpacing

@Composable
fun ExpertOmoteGakiScreen(
    templateId: String,
    onNavigateToTextInput: (templateId: String, omoteGaki: String) -> Unit,
    onBackClick: () -> Unit
) {
    val template = NoshiTemplate.findById(templateId) ?: return
    var freeInput by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            NoshiTopBar(title = "表書きを選択", onBackClick = onBackClick)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = NoshiSpacing.spacingLG, vertical = NoshiSpacing.spacingMD)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingMD)
        ) {
            Text(text = template.name, style = MaterialTheme.typography.titleMedium)
            Text(
                text = template.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(NoshiSpacing.spacingSM))

            Text(
                text = "おすすめの表書きはこちらになります。選んでください",
                style = MaterialTheme.typography.bodyMedium
            )

            template.omoteGakiCandidates.forEach { candidate ->
                NoshiChoiceButton(
                    text = candidate,
                    onClick = { onNavigateToTextInput(templateId, candidate) }
                )
            }

            Spacer(modifier = Modifier.height(NoshiSpacing.spacingMD))

            Text(text = "または自由に入力", style = MaterialTheme.typography.titleSmall)
            OutlinedTextField(
                value = freeInput,
                onValueChange = { freeInput = it },
                placeholder = { Text("例: 御祝") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Text(
                text = "のし紙の上部に表示される用途を示すテキストです",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(NoshiSpacing.spacingSM))

            NoshiPrimaryButton(
                text = "次へ（名前入力）",
                onClick = { onNavigateToTextInput(templateId, freeInput) },
                enabled = freeInput.trim().isNotEmpty()
            )
        }
    }
}
