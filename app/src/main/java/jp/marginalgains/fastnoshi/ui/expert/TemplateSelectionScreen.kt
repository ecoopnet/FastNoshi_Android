package jp.marginalgains.fastnoshi.ui.expert

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import jp.marginalgains.fastnoshi.domain.model.NoshiTemplate
import jp.marginalgains.fastnoshi.ui.components.NoshiTopBar

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
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "上級者モード",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "のし紙の種類を直接選択できます。用途に合わせて適切なテンプレートをお選びください。",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(4.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.templates) { template ->
                    TemplateCard(
                        template = template,
                        onClick = { viewModel.onTemplateSelected(template.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TemplateCard(template: NoshiTemplate, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = template.name,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center
            )
            Text(
                text = template.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
