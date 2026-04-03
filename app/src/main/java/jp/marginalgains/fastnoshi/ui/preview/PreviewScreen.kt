package jp.marginalgains.fastnoshi.ui.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import jp.marginalgains.fastnoshi.domain.model.NoshiFontSet
import jp.marginalgains.fastnoshi.ui.components.NoshiPrimaryButton
import jp.marginalgains.fastnoshi.ui.components.NoshiTopBar

private val PAPER_SIZES = listOf("A4", "B4", "A3")
private const val MIN_FONT_SIZE = 12f
private const val MAX_FONT_SIZE = 48f

@Composable
fun PreviewScreen(
    templateId: String,
    omoteGaki: String,
    names: List<String>,
    onNavigateToPrint: (NavigateToPrint) -> Unit,
    onBackClick: () -> Unit,
    viewModel: PreviewViewModel = viewModel()
) {
    LaunchedEffect(templateId) {
        viewModel.init(templateId = templateId, omoteGaki = omoteGaki, names = names)
    }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.navigateToPrint) {
        uiState.navigateToPrint?.let { nav ->
            onNavigateToPrint(nav)
            viewModel.onNavigationConsumed()
        }
    }

    Scaffold(
        topBar = {
            NoshiTopBar(title = "印刷デザイン", onBackClick = onBackClick)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            NoshiPreviewPlaceholder(
                omoteGaki = uiState.omoteGaki,
                names = uiState.names
            )

            PaperSizeSelector(
                selected = uiState.selectedPaperSize,
                onChanged = viewModel::onPaperSizeChanged
            )

            FontSelector(
                selected = uiState.selectedFontSet,
                onChanged = viewModel::onFontSetChanged
            )

            FontSizeSlider(
                label = "表書きサイズ",
                value = uiState.omoteGakiFontSize,
                onChanged = viewModel::onOmoteGakiFontSizeChanged
            )

            FontSizeSlider(
                label = "名前サイズ",
                value = uiState.nameFontSize,
                onChanged = viewModel::onNameFontSizeChanged
            )

            PriceInfo(
                isColor = uiState.isColorTemplate,
                price = uiState.price
            )

            NoshiPrimaryButton(
                text = "印刷予約へ",
                onClick = viewModel::onRequestPrint
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun NoshiPreviewPlaceholder(omoteGaki: String, names: List<String>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(297f / 210f)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = omoteGaki,
                style = MaterialTheme.typography.headlineMedium
            )
            names.forEach { name ->
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun PaperSizeSelector(selected: String, onChanged: (String) -> Unit) {
    Column {
        Text(text = "用紙サイズ", style = MaterialTheme.typography.titleSmall)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 4.dp)
        ) {
            PAPER_SIZES.forEach { size ->
                FilterChip(
                    selected = selected == size,
                    onClick = { onChanged(size) },
                    label = { Text(size) }
                )
            }
        }
    }
}

@Composable
private fun FontSelector(selected: NoshiFontSet, onChanged: (NoshiFontSet) -> Unit) {
    Column {
        Text(text = "書体", style = MaterialTheme.typography.titleSmall)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 4.dp)
        ) {
            NoshiFontSet.all.forEach { fontSet ->
                FilterChip(
                    selected = selected.id == fontSet.id,
                    onClick = { onChanged(fontSet) },
                    label = { Text(fontSet.displayName) }
                )
            }
        }
    }
}

@Composable
private fun FontSizeSlider(label: String, value: Float, onChanged: (Float) -> Unit) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.titleSmall)
            Text(
                text = "${value.toInt()}pt",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Slider(
            value = value,
            onValueChange = onChanged,
            valueRange = MIN_FONT_SIZE..MAX_FONT_SIZE,
            steps = ((MAX_FONT_SIZE - MIN_FONT_SIZE) / 2 - 1).toInt()
        )
    }
}

@Composable
private fun PriceInfo(isColor: Boolean, price: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "印刷料金",
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = if (isColor) "カラー印刷" else "白黒印刷",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = "¥$price",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
