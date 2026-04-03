package jp.marginalgains.fastnoshi.ui.guide

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import jp.marginalgains.fastnoshi.domain.model.NoshiTemplate
import jp.marginalgains.fastnoshi.ui.components.NoshiTopBar
import jp.marginalgains.fastnoshi.ui.theme.NoshiSpacing

@Composable
fun MannersGuideScreen(onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            NoshiTopBar(title = "のしマナーガイド", onBackClick = onBackClick)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = NoshiSpacing.spacingMD)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingLG)
        ) {
            Spacer(modifier = Modifier.height(NoshiSpacing.spacingSM))

            // ヘッダーセクション
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingSM)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "のしのマナーガイド",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "のし紙は用途に応じて水引の種類が異なります。\n間違えると失礼にあたるため、適切なテンプレートを選びましょう。",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            GuideIntroSection()

            NoshiTemplate.all.forEach { template ->
                TemplateGuideCard(template)
            }

            NameWritingGuideSection()

            Spacer(modifier = Modifier.height(NoshiSpacing.spacingMD))
        }
    }
}

@Composable
private fun GuideIntroSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(NoshiSpacing.spacingMD)) {
            Text(
                text = "のし紙の選び方",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(NoshiSpacing.spacingSM))
            Text(
                text = "のし紙は用途に応じて水引の種類が異なります。" +
                    "間違えると失礼にあたるため、適切なテンプレートを選びましょう。",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun TemplateGuideCard(template: NoshiTemplate) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(NoshiSpacing.spacingMD)) {
            Text(
                text = template.name,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(NoshiSpacing.spacingXS))
            Text(
                text = template.description,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(NoshiSpacing.spacingSM))
            Text(
                text = "印刷: ${if (template.isColor) "カラー（¥200）" else "白黒（¥100）"}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(NoshiSpacing.spacingSM))
            Text(
                text = "代表的な表書き",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(NoshiSpacing.spacingXS))
            Text(
                text = template.omoteGakiCandidates.joinToString("、"),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun NameWritingGuideSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(NoshiSpacing.spacingMD)) {
            Text(
                text = "名前の書き方",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(NoshiSpacing.spacingSM))
            Text(
                text = "・個人名：フルネームを1名記入\n" +
                    "・連名（2〜3名）：右から目上の方の順に記入\n" +
                    "・連名（4名以上）：代表者名＋「外一同」と記入\n" +
                    "・会社名：代表者の肩書き・氏名を記入",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
