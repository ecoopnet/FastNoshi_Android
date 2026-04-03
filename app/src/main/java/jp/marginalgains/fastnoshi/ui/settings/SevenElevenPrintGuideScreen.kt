package jp.marginalgains.fastnoshi.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.CurrencyYen
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.FindInPage
import androidx.compose.material.icons.outlined.Numbers
import androidx.compose.material.icons.outlined.Store
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import jp.marginalgains.fastnoshi.ui.components.NoshiTopBar
import jp.marginalgains.fastnoshi.ui.theme.NoshiRadius
import jp.marginalgains.fastnoshi.ui.theme.NoshiSpacing

@Composable
fun SevenElevenPrintGuideScreen(onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            NoshiTopBar(title = "印刷方法", onBackClick = onBackClick)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = NoshiSpacing.spacingLG, vertical = NoshiSpacing.spacingLG),
            verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingXL)
        ) {
            // Header
            Column(verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingSM)) {
                Text(
                    text = "セブン‐イレブンでの印刷方法",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "アプリで予約番号を取得したら、お近くのセブン‐イレブンで印刷できます。",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Steps
            Column(verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingLG)) {
                PrintGuideStep(
                    number = 1,
                    icon = Icons.Outlined.Upload,
                    title = "アプリで予約番号を取得",
                    lines = listOf("予約番号は、プレビュー画面で確認できます。")
                )
                PrintGuideStep(
                    number = 2,
                    icon = Icons.Outlined.Store,
                    title = "セブン‐イレブンへ行く",
                    lines = listOf("お近くのセブン‐イレブンのマルチコピー機へ向かいます。")
                )
                PrintGuideStep(
                    number = 3,
                    icon = Icons.Outlined.TouchApp,
                    title = "「プリント」→「ネットプリント」を選択",
                    lines = listOf(
                        "マルチコピー機のタッチパネルで「プリント」を押します。",
                        "次に「ネットプリント」を押します。"
                    )
                )
                PrintGuideStep(
                    number = 4,
                    icon = Icons.Outlined.Numbers,
                    title = "予約番号を入力",
                    lines = listOf(
                        "8桁のプリント予約番号をタッチパネルで入力します。",
                        "入力後「確認」を押すとファイルのダウンロードが始まります。"
                    )
                )
                PrintGuideStep(
                    number = 5,
                    icon = Icons.Outlined.FindInPage,
                    title = "内容を確認",
                    lines = listOf(
                        "プレビューが表示されるので、印刷内容と料金を確認します。",
                        "問題なければ「これで決定」を押します。"
                    )
                )
                PrintGuideStep(
                    number = 6,
                    icon = Icons.Outlined.Description,
                    title = "用紙サイズを確認",
                    lines = listOf(
                        "A4、A3、B4のいずれかの用紙サイズを選択してください。",
                        "贈り物のサイズに合わせて選びます。"
                    )
                )
                PrintGuideStep(
                    number = 7,
                    icon = Icons.Outlined.CurrencyYen,
                    title = "お支払い・印刷",
                    lines = listOf(
                        "「コインでお支払い」または「nanacoでお支払い」を選びます。",
                        "料金は白黒100円、カラー200円です。",
                        "「プリントスタート」を押せば印刷開始です。",
                        "印刷物とおつりの取り忘れにご注意ください。"
                    )
                )
                PrintGuideStep(
                    number = 8,
                    icon = Icons.Outlined.CardGiftcard,
                    title = "贈り物に貼って完成",
                    lines = listOf("印刷されたのし紙を贈り物に貼れば完成です。")
                )
            }

            // Notes
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(NoshiRadius.radiusSM)
            ) {
                Column(
                    modifier = Modifier.padding(NoshiSpacing.spacingMD),
                    verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingSM)
                ) {
                    Text(
                        text = "補足",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "・予約番号の有効期限にご注意ください\n" +
                            "・同じのし紙を複数枚印刷したい場合は「部数」を変更できます\n" +
                            "・領収書の発行も可能です",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        lineHeight = MaterialTheme.typography.bodySmall.lineHeight
                    )
                }
            }
        }
    }
}

@Composable
private fun PrintGuideStep(
    number: Int,
    icon: ImageVector,
    title: String,
    lines: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(NoshiRadius.radiusMD)
    ) {
        Row(
            modifier = Modifier.padding(NoshiSpacing.spacingMD),
            horizontalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingMD)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$number",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    lines.forEach { line ->
                        Text(
                            text = line,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
