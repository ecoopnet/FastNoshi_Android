package jp.marginalgains.fastnoshi.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import jp.marginalgains.fastnoshi.ui.components.NoshiTopBar
import jp.marginalgains.fastnoshi.ui.theme.NoshiRadius
import jp.marginalgains.fastnoshi.ui.theme.NoshiSpacing

@Composable
fun PrintInfoScreen(onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            NoshiTopBar(title = "対応コンビニ・料金", onBackClick = onBackClick)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(NoshiSpacing.spacingMD),
            verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingMD)
        ) {
            StoreInfoCard(
                name = "セブン‐イレブン",
                service = "ネットプリント",
                whitePrice = "白黒 100円/枚",
                colorPrice = "カラー 200円/枚",
                note = "A4、A3、B4サイズに対応"
            )

            Column(
                modifier = Modifier.padding(NoshiSpacing.spacingMD),
                verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingSM)
            ) {
                Text(
                    text = "注意事項",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "・印刷料金はコンビニのマルチコピー機で直接お支払いください\n" +
                        "・贈り物のサイズに合わせて用紙サイズを選択してください\n" +
                        "・店舗の複合機により色味に多少の差異が生じる場合があります\n" +
                        "・のし紙は贈り物に貼ってご使用ください",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun StoreInfoCard(
    name: String,
    service: String,
    whitePrice: String,
    colorPrice: String,
    note: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(NoshiRadius.radiusMD),
        elevation = CardDefaults.cardElevation(defaultElevation = NoshiSpacing.spacingXS)
    ) {
        Column(
            modifier = Modifier.padding(NoshiSpacing.spacingMD),
            verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingSM)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "対応サービス",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = service,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "料金",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                    Text(
                        text = whitePrice,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = colorPrice,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }

            Text(
                text = note,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}
