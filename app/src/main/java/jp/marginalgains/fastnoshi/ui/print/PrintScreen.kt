package jp.marginalgains.fastnoshi.ui.print

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import jp.marginalgains.fastnoshi.ui.components.NoshiPrimaryButton
import jp.marginalgains.fastnoshi.ui.components.NoshiSecondaryButton
import jp.marginalgains.fastnoshi.ui.components.NoshiTopBar

@Composable
fun PrintScreen(
    templateId: String,
    omoteGaki: String,
    names: List<String>,
    fontSetId: String,
    omoteGakiFontSize: Float,
    nameFontSize: Float,
    paperSize: String,
    onNavigateHome: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: PrintViewModel = viewModel()
) {
    LaunchedEffect(templateId) {
        viewModel.init(
            templateId = templateId,
            omoteGaki = omoteGaki,
            names = names,
            fontSetId = fontSetId,
            omoteGakiFontSize = omoteGakiFontSize,
            nameFontSize = nameFontSize,
            paperSize = paperSize
        )
    }

    val uiState by viewModel.uiState.collectAsState()

    @Suppress("DEPRECATION")
    val clipboardManager = LocalClipboardManager.current

    LaunchedEffect(uiState.navigateToHome) {
        if (uiState.navigateToHome) {
            onNavigateHome()
        }
    }

    Scaffold(
        topBar = {
            NoshiTopBar(title = "印刷予約", onBackClick = onBackClick)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.isUploading) {
                UploadingSection()
            } else if (uiState.printId != null) {
                ReservationSection(
                    printId = uiState.printId!!,
                    expiresAt = uiState.expiresAt,
                    onCopy = {
                        clipboardManager.setText(AnnotatedString(uiState.printId!!))
                        viewModel.onCopyPrintId()
                    },
                    onHome = viewModel::onNavigateHome
                )
            } else {
                InstructionSection(paperSize = uiState.paperSize)

                uiState.errorMessage?.let { error ->
                    ErrorSection(message = error)
                }

                NoshiPrimaryButton(
                    text = "印刷予約を送信",
                    onClick = {
                        viewModel.onUploadStarted()
                        // TODO: NpsRepository.upload() を呼び出す（レンダリングエンジン統合後）
                    }
                )
            }

            if (uiState.showCopiedFeedback) {
                Text(
                    text = "予約番号をコピーしました",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun UploadingSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 48.dp)
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "送信中...", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun ReservationSection(
    printId: String,
    expiresAt: String?,
    onCopy: () -> Unit,
    onHome: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "印刷予約が完了しました",
            style = MaterialTheme.typography.headlineSmall
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "予約番号",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = printId,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                if (expiresAt != null) {
                    Text(
                        text = "有効期限: $expiresAt",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        NoshiSecondaryButton(
            text = "予約番号をコピー",
            onClick = onCopy
        )

        Text(
            text = "予約番号を控えて、セブン‐イレブンのマルチコピー機で印刷してください",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        InstructionSteps()

        Spacer(modifier = Modifier.height(16.dp))

        NoshiPrimaryButton(
            text = "ホームに戻る",
            onClick = onHome
        )
    }
}

@Composable
private fun InstructionSection(paperSize: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "セブン‐イレブンで印刷",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "用紙サイズ $paperSize で印刷予約を行います。",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun InstructionSteps() {
    val steps = listOf(
        "セブン‐イレブンのマルチコピー機へ",
        "「プリント」→「ネットプリント」を選択",
        "予約番号を入力して内容を確認",
        "選択した用紙サイズで印刷"
    )
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(text = "印刷手順", style = MaterialTheme.typography.titleSmall)
        steps.forEachIndexed { index, step ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "${index + 1}.",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = step,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun ErrorSection(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onErrorContainer,
            modifier = Modifier.padding(16.dp)
        )
    }
}
