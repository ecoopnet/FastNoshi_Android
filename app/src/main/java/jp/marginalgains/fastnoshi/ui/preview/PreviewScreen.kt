package jp.marginalgains.fastnoshi.ui.preview

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import jp.marginalgains.fastnoshi.domain.model.NoshiFontSet
import jp.marginalgains.fastnoshi.domain.model.NoshiTemplate
import jp.marginalgains.fastnoshi.rendering.FontResolver
import jp.marginalgains.fastnoshi.rendering.NoshiRenderer
import jp.marginalgains.fastnoshi.rendering.PaperSize
import jp.marginalgains.fastnoshi.ui.components.NoshiPrimaryButton
import jp.marginalgains.fastnoshi.ui.components.NoshiTopBar
import jp.marginalgains.fastnoshi.ui.theme.NoshiRadius
import jp.marginalgains.fastnoshi.ui.theme.NoshiSpacing

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
    val context = LocalContext.current

    LaunchedEffect(templateId) {
        viewModel.init(templateId = templateId, omoteGaki = omoteGaki, names = names)
    }

    val uiState by viewModel.uiState.collectAsState()

    val previewBitmap by produceState<Bitmap?>(
        initialValue = null,
        uiState.templateId,
        uiState.omoteGaki,
        uiState.names,
        uiState.selectedFontSet,
        uiState.omoteGakiFontSize,
        uiState.nameFontSize,
        uiState.selectedPaperSize
    ) {
        val template = NoshiTemplate.findById(uiState.templateId) ?: return@produceState
        val paperSize = PaperSize.fromString(uiState.selectedPaperSize)
            ?: PaperSize.A4
        val typeface = FontResolver.resolve(uiState.selectedFontSet)
        val renderer = NoshiRenderer(context)
        value = renderer.renderToBitmap(
            template = template,
            omoteGaki = uiState.omoteGaki,
            names = uiState.names,
            typeface = typeface,
            omoteGakiFontSize = uiState.omoteGakiFontSize,
            nameFontSize = uiState.nameFontSize,
            paperSize = paperSize
        )
    }

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
                .padding(horizontal = NoshiSpacing.spacingMD)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingLG)
        ) {
            Spacer(modifier = Modifier.height(NoshiSpacing.spacingSM))

            NoshiPreviewImage(bitmap = previewBitmap)

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

            Spacer(modifier = Modifier.height(NoshiSpacing.spacingMD))
        }
    }
}

@Composable
private fun NoshiPreviewImage(bitmap: Bitmap?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(NoshiRadius.radiusMD),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "のし紙プレビュー",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(297f / 210f),
                contentScale = ContentScale.Fit
            )
        } else {
            Text(
                text = "プレビュー読み込み中...",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(297f / 210f)
                    .padding(NoshiSpacing.spacingMD),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun PaperSizeSelector(selected: String, onChanged: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(NoshiRadius.radiusMD),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(NoshiSpacing.spacingMD)) {
            Text(
                text = "用紙サイズ",
                style = MaterialTheme.typography.titleSmall
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingSM),
                modifier = Modifier.padding(top = NoshiSpacing.spacingSM)
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
}

@Composable
private fun FontSelector(selected: NoshiFontSet, onChanged: (NoshiFontSet) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(NoshiRadius.radiusMD),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(NoshiSpacing.spacingMD)) {
            Text(text = "書体", style = MaterialTheme.typography.titleSmall)
            Row(
                horizontalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingSM),
                modifier = Modifier.padding(top = NoshiSpacing.spacingSM)
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
}

@Composable
private fun FontSizeSlider(label: String, value: Float, onChanged: (Float) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(NoshiRadius.radiusMD),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(NoshiSpacing.spacingMD)) {
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
                steps = ((MAX_FONT_SIZE - MIN_FONT_SIZE) / 2 - 1).toInt(),
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.secondary,
                    activeTrackColor = MaterialTheme.colorScheme.secondary
                )
            )
        }
    }
}

@Composable
private fun PriceInfo(isColor: Boolean, price: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(NoshiRadius.radiusMD),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(NoshiSpacing.spacingMD),
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
}
