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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocalPrintshop
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material.icons.outlined.Store
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import jp.marginalgains.fastnoshi.BuildConfig
import jp.marginalgains.fastnoshi.ui.components.NoshiTopBar
import jp.marginalgains.fastnoshi.ui.theme.NoshiSpacing

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onNavigateToPrintGuide: () -> Unit = {},
    onNavigateToPrintInfo: () -> Unit = {}
) {
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            NoshiTopBar(title = "使い方・設定", onBackClick = onBackClick)
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

            SettingsSection(title = "印刷方法") {
                SettingsRow(
                    icon = Icons.Outlined.LocalPrintshop,
                    text = "セブン‐イレブンでの印刷方法",
                    onClick = onNavigateToPrintGuide
                )
            }

            SettingsSection(title = "印刷について") {
                SettingsRow(
                    icon = Icons.Outlined.Store,
                    text = "対応コンビニ・料金",
                    onClick = onNavigateToPrintInfo
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = NoshiSpacing.spacingSM))
                SettingsRow(
                    icon = Icons.Outlined.Info,
                    text = "ネットプリントからのお知らせ",
                    onClick = {
                        uriHandler.openUri("https://www.printing.ne.jp/cgi-bin/fb/m_ad/msg.cgi")
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = NoshiSpacing.spacingSM))
                SettingsRow(
                    icon = Icons.Outlined.Description,
                    text = "ネットプリント利用規約",
                    onClick = {
                        uriHandler.openUri("https://www.printing.ne.jp/support/bp/eula_bp.html")
                    }
                )
            }

            SettingsSection(title = "サポート") {
                SettingsRow(
                    icon = Icons.Outlined.Email,
                    text = "お問い合わせ",
                    onClick = {
                        uriHandler.openUri(
                            "https://docs.google.com/forms/d/e/" +
                                "1FAIpQLScyI2EONzFEEWhxKhg4sR90Z__hZ5sgFbLeLLquExzsCmbosA" +
                                "/viewform?usp=dialog"
                        )
                    }
                )
            }

            SettingsSection(title = "アプリについて") {
                InfoRow(label = "バージョン", value = BuildConfig.VERSION_NAME)
                HorizontalDivider(modifier = Modifier.padding(horizontal = NoshiSpacing.spacingSM))
                SettingsRow(
                    icon = Icons.Outlined.PrivacyTip,
                    text = "プライバシーポリシー",
                    onClick = {
                        uriHandler.openUri("https://marginalgains.github.io/privacy-policy.html")
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = NoshiSpacing.spacingSM))
                SettingsRow(
                    icon = Icons.Outlined.Description,
                    text = "利用規約",
                    onClick = {
                        uriHandler.openUri(
                            "https://marginalgains.github.io/10min-noshi/term-of-services.html"
                        )
                    }
                )
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
            Column(modifier = Modifier.padding(vertical = NoshiSpacing.spacingXS)) {
                content()
            }
        }
    }
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = NoshiSpacing.spacingMD),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingSM)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = NoshiSpacing.spacingMD),
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
