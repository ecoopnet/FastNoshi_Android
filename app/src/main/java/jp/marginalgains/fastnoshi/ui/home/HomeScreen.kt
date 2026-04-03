package jp.marginalgains.fastnoshi.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.marginalgains.fastnoshi.ui.components.NoshiPrimaryButton
import jp.marginalgains.fastnoshi.ui.components.NoshiSecondaryButton

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
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "10分のし",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(40.dp))
            NoshiPrimaryButton(
                text = "のし紙を作る",
                onClick = onGuidedFlowClick
            )
            Spacer(modifier = Modifier.height(12.dp))
            NoshiSecondaryButton(
                text = "テンプレートから選ぶ",
                onClick = onExpertClick
            )
            Spacer(modifier = Modifier.height(12.dp))
            NoshiSecondaryButton(
                text = "のしマナーガイド",
                onClick = onMannersGuideClick
            )
            Spacer(modifier = Modifier.height(12.dp))
            NoshiSecondaryButton(
                text = "作成履歴",
                onClick = onHistoryClick
            )
            Spacer(modifier = Modifier.height(12.dp))
            NoshiSecondaryButton(
                text = "設定",
                onClick = onSettingsClick
            )
        }
    }
}
