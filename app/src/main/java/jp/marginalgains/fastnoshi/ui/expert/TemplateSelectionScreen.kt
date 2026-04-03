package jp.marginalgains.fastnoshi.ui.expert

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import jp.marginalgains.fastnoshi.domain.model.NoshiTemplate
import jp.marginalgains.fastnoshi.ui.components.NoshiTopBar
import jp.marginalgains.fastnoshi.ui.theme.NoshiRadius
import jp.marginalgains.fastnoshi.ui.theme.NoshiSpacing

@Composable
fun TemplateSelectionScreen(
    onNavigateToOmoteGaki: (templateId: String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: ExpertViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.navigateToOmoteGaki) {
        uiState.navigateToOmoteGaki?.let { templateId ->
            onNavigateToOmoteGaki(templateId)
            viewModel.onNavigationConsumed()
        }
    }

    Scaffold(
        topBar = {
            NoshiTopBar(title = "のし紙の種類を選択", onBackClick = onBackClick)
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
                .verticalScroll(rememberScrollState())
        ) {
            // 説明セクション
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = NoshiSpacing.spacingMD),
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
                    verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingSM)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingSM)
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.secondary.copy(
                                        alpha = 0.12f
                                    ),
                                    shape = RoundedCornerShape(NoshiRadius.radiusSM)
                                )
                                .padding(12.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingXS)
                        ) {
                            Text(
                                text = "上級者モード",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "のし紙の種類を直接選択できます。用途に合わせて適切なテンプレートをお選びください。",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // テンプレートグリッド (2列)
            val templates = uiState.templates
            val rows = templates.chunked(2)
            Column(
                verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingMD)
            ) {
                rows.forEach { rowTemplates ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingMD),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        rowTemplates.forEach { template ->
                            TemplateCard(
                                template = template,
                                onClick = { viewModel.onTemplateSelected(template.id) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        // 奇数の場合にスペースを埋める
                        if (rowTemplates.size == 1) {
                            Box(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TemplateCard(
    template: NoshiTemplate,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageBitmap by produceState<ImageBitmap?>(initialValue = null, template.pngFileName) {
        value = try {
            context.assets.open("templates/${template.pngFileName}").use { stream ->
                BitmapFactory.decodeStream(stream)?.asImageBitmap()
            }
        } catch (_: Exception) {
            null
        }
    }

    Card(
        onClick = onClick,
        modifier = modifier,
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingSM)
        ) {
            // PNG画像プレビュー
            val imageModifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f / 1.4f)
                .clip(RoundedCornerShape(NoshiRadius.radiusSM))
                .background(MaterialTheme.colorScheme.surfaceVariant)

            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap!!,
                    contentDescription = template.name,
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = imageModifier,
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = template.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = template.name,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = template.description,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
