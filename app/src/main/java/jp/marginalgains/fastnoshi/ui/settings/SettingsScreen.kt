package jp.marginalgains.fastnoshi.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jp.marginalgains.fastnoshi.BuildConfig
import jp.marginalgains.fastnoshi.domain.model.NoshiFontSet
import jp.marginalgains.fastnoshi.ui.components.NoshiTopBar
import jp.marginalgains.fastnoshi.ui.theme.NoshiSpacing

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            NoshiTopBar(title = "設定", onBackClick = onBackClick)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = NoshiSpacing.spacingMD)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingMD)
        ) {
            Spacer(modifier = Modifier.height(NoshiSpacing.spacingSM))

            SettingsSection(title = "デフォルトフォント") {
                NoshiFontSet.all.forEach { font ->
                    RadioRow(
                        text = font.displayName,
                        selected = uiState.defaultFontId == font.id,
                        onClick = { viewModel.updateDefaultFont(font.id) }
                    )
                }
            }

            SettingsSection(title = "デフォルト用紙サイズ") {
                listOf("A4", "B4", "A3").forEach { size ->
                    RadioRow(
                        text = size,
                        selected = uiState.defaultPaperSize == size,
                        onClick = { viewModel.updateDefaultPaperSize(size) }
                    )
                }
            }

            SettingsSection(title = "アプリ情報") {
                InfoRow(label = "バージョン", value = BuildConfig.VERSION_NAME)
                HorizontalDivider()
                InfoRow(label = "ビルド", value = BuildConfig.VERSION_CODE.toString())
            }

            Spacer(modifier = Modifier.height(NoshiSpacing.spacingMD))
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = NoshiSpacing.spacingSM)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                content()
            }
        }
    }
}

@Composable
private fun RadioRow(text: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
