package jp.marginalgains.fastnoshi.ui.input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import jp.marginalgains.fastnoshi.ui.components.NoshiPrimaryButton
import jp.marginalgains.fastnoshi.ui.components.NoshiTopBar

@Composable
fun TextInputScreen(
    templateId: String,
    omoteGaki: String,
    onNavigateToPreview: (templateId: String, omoteGaki: String, names: List<String>) -> Unit,
    onBackClick: () -> Unit,
    viewModel: TextInputViewModel = viewModel()
) {
    LaunchedEffect(templateId, omoteGaki) {
        viewModel.init(templateId = templateId, omoteGaki = omoteGaki)
    }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.navigateToPreview) {
        uiState.navigateToPreview?.let { nav ->
            onNavigateToPreview(nav.templateId, nav.omoteGaki, nav.names)
            viewModel.onNavigationConsumed()
        }
    }

    Scaffold(
        topBar = {
            NoshiTopBar(title = "のし紙の内容を入力", onBackClick = onBackClick)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OmoteGakiSection(
                omoteGaki = uiState.omoteGaki,
                onChanged = viewModel::onOmoteGakiChanged
            )

            NameSection(
                names = uiState.names,
                canAddName = uiState.canAddName,
                onNameChanged = viewModel::onNameChanged,
                onAddName = viewModel::onAddName,
                onRemoveName = viewModel::onRemoveName
            )

            Spacer(modifier = Modifier.height(8.dp))

            NoshiPrimaryButton(
                text = "プレビュー",
                onClick = viewModel::onProceed,
                enabled = uiState.canProceed
            )
        }
    }
}

@Composable
private fun OmoteGakiSection(omoteGaki: String, onChanged: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "表書き", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "必須",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error
            )
        }
        OutlinedTextField(
            value = omoteGaki,
            onValueChange = onChanged,
            placeholder = { Text("例: 御祝") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Text(
            text = "のし紙の上部に表示される用途を示すテキストです",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun NameSection(
    names: List<String>,
    canAddName: Boolean,
    onNameChanged: (Int, String) -> Unit,
    onAddName: () -> Unit,
    onRemoveName: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "贈り主の名前", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "必須",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${names.size} / 5名",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        names.forEachIndexed { index, name ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${index + 1}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.width(20.dp)
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { onNameChanged(index, it) },
                    placeholder = { Text("名前を入力") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                if (names.size > 1) {
                    IconButton(onClick = { onRemoveName(index) }) {
                        Icon(
                            imageVector = Icons.Default.RemoveCircle,
                            contentDescription = "削除",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }

        if (canAddName) {
            IconButton(onClick = onAddName) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "名前を追加",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Text(
            text = "のし紙の下部に表示される贈り主の名前です（受取人ではありません）",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
