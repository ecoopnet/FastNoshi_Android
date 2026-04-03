package jp.marginalgains.fastnoshi.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import jp.marginalgains.fastnoshi.ui.theme.NoshiRadius
import jp.marginalgains.fastnoshi.ui.theme.NoshiSpacing

@Composable
fun HomeScreen(
    onGuidedFlowClick: () -> Unit,
    onExpertClick: () -> Unit,
    onMannersGuideClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = NoshiSpacing.spacingMD,
                    vertical = NoshiSpacing.spacingMD
                ),
            verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingLG)
        ) {
            // ヘッダーセクション
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = NoshiSpacing.spacingLG,
                        bottom = NoshiSpacing.spacingSM
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingSM)
            ) {
                Icon(
                    imageVector = Icons.Default.CardGiftcard,
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(bottom = NoshiSpacing.spacingSM),
                    tint = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "10分のし",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingXS)
                ) {
                    Text(
                        text = "セブン\u2010イレブンで簡単のし紙印刷",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "質問に答えるだけで適切なのし紙を作成",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "カラー200円/枚、白黒100円/枚",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // メニューセクション
            Column(
                verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingMD)
            ) {
                MenuRow(
                    icon = Icons.Default.AutoAwesome,
                    iconColor = MaterialTheme.colorScheme.secondary,
                    title = "のしの作成",
                    subtitle = "かんたんな質問に答えて、自動選択",
                    onClick = onGuidedFlowClick
                )
                MenuRow(
                    icon = Icons.Default.GridView,
                    iconColor = MaterialTheme.colorScheme.primary,
                    title = "のしの選択（自分で作成）",
                    subtitle = "テンプレートから直接選択",
                    onClick = onExpertClick
                )
                MenuRow(
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    iconColor = MaterialTheme.colorScheme.tertiary,
                    title = "のしのマナー",
                    subtitle = "のしの使い方とマナーを学ぶ",
                    onClick = onMannersGuideClick
                )
                MenuRow(
                    icon = Icons.Default.Settings,
                    iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    title = "設定",
                    subtitle = "アプリの設定",
                    onClick = onSettingsClick
                )
                MenuRow(
                    icon = Icons.Default.History,
                    iconColor = MaterialTheme.colorScheme.primary,
                    title = "履歴",
                    subtitle = "作成したのし紙の確認",
                    onClick = onHistoryClick
                )
            }
        }
    }
}

@Composable
private fun MenuRow(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(NoshiRadius.radiusMD),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(NoshiSpacing.spacingMD),
            horizontalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingMD),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        color = iconColor.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(NoshiRadius.radiusSM)
                    )
                    .padding(12.dp),
                tint = iconColor
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingXS)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}
