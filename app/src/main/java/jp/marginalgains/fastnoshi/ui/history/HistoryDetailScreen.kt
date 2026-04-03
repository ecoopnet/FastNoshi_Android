package jp.marginalgains.fastnoshi.ui.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jp.marginalgains.fastnoshi.domain.model.NoshiFontSet
import jp.marginalgains.fastnoshi.domain.model.NoshiPaper
import jp.marginalgains.fastnoshi.domain.model.NoshiTemplate
import jp.marginalgains.fastnoshi.ui.components.NoshiTopBar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryDetailScreen(
    onBackClick: () -> Unit,
    viewModel: HistoryDetailViewModel = hiltViewModel()
) {
    val paper by viewModel.paper.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            NoshiTopBar(title = "履歴詳細", onBackClick = onBackClick)
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (paper == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                HistoryDetailContent(paper = paper!!)
            }
        }
    }
}

@Composable
private fun HistoryDetailContent(paper: NoshiPaper) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DetailRow(label = "テンプレート", value = NoshiTemplate.findById(paper.templateId)?.name ?: "不明")
                HorizontalDivider()
                DetailRow(label = "表書き", value = paper.omoteGaki)
                HorizontalDivider()
                DetailRow(label = "名前", value = paper.names.joinToString("、"))
                HorizontalDivider()
                DetailRow(label = "フォント", value = NoshiFontSet.findById(paper.fontId)?.displayName ?: paper.fontId)
                HorizontalDivider()
                DetailRow(label = "フォントサイズ", value = "${paper.fontSize.toInt()}pt")
                HorizontalDivider()
                DetailRow(label = "用紙サイズ", value = paper.paperSize)
                HorizontalDivider()
                DetailRow(label = "作成日時", value = formatDateTime(paper.createdAt))
                if (paper.lastPrintedAt != null) {
                    HorizontalDivider()
                    DetailRow(label = "印刷日時", value = formatDateTime(paper.lastPrintedAt!!))
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

private fun formatDateTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.JAPAN)
    return sdf.format(Date(timestamp))
}
