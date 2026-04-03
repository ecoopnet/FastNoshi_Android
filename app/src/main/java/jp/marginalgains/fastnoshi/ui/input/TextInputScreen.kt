package jp.marginalgains.fastnoshi.ui.input

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import jp.marginalgains.fastnoshi.ui.components.NoshiPrimaryButton
import jp.marginalgains.fastnoshi.ui.components.NoshiTopBar
import jp.marginalgains.fastnoshi.ui.theme.NoshiRadius
import jp.marginalgains.fastnoshi.ui.theme.NoshiSpacing

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
                .padding(
                    horizontal = NoshiSpacing.spacingLG,
                    vertical = NoshiSpacing.spacingMD
                )
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingLG)
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

            NoshiPrimaryButton(
                text = "プレビュー",
                onClick = viewModel::onProceed,
                enabled = uiState.canProceed
            )

            Spacer(modifier = Modifier.height(NoshiSpacing.spacingMD))
        }
    }
}

@Composable
private fun OmoteGakiSection(omoteGaki: String, onChanged: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(NoshiRadius.radiusMD),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(NoshiSpacing.spacingMD),
            verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingMD)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "表書き",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(NoshiSpacing.spacingSM))
                Badge(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.White
                ) {
                    Text(
                        text = "必須",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(
                            horizontal = NoshiSpacing.spacingSM,
                            vertical = 2.dp
                        )
                    )
                }
            }

            OutlinedTextField(
                value = omoteGaki,
                onValueChange = onChanged,
                placeholder = { Text("例: 御祝") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(NoshiRadius.radiusSM)
            )

            Text(
                text = "のし紙の上部に表示される用途を示すテキストです",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(NoshiRadius.radiusMD),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(NoshiSpacing.spacingMD),
            verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingMD)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "贈り主の名前",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(NoshiSpacing.spacingSM))
                Badge(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.White
                ) {
                    Text(
                        text = "必須",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(
                            horizontal = NoshiSpacing.spacingSM,
                            vertical = 2.dp
                        )
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${names.size} / 5名",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (names.size > 5) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }

            names.forEachIndexed { index, name ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = NoshiSpacing.spacingXS),
                    horizontalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingSM)
                ) {
                    Text(
                        text = "${index + 1}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.width(20.dp)
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = { onNameChanged(index, it) },
                        placeholder = { Text("名前を入力") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(NoshiRadius.radiusSM)
                    )

                    if (names.size > 1) {
                        IconButton(
                            onClick = { onRemoveName(index) },
                            modifier = Modifier.size(40.dp)
                        ) {
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onAddName() }
                        .padding(NoshiSpacing.spacingSM)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "名前を追加",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(NoshiSpacing.spacingSM))
                    Text(
                        text = "名前を追加",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Text(
                text = "のし紙の下部に表示される贈り主の名前です（受取人ではありません）",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
