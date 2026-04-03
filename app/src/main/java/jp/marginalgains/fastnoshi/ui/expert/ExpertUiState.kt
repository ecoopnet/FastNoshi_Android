package jp.marginalgains.fastnoshi.ui.expert

import jp.marginalgains.fastnoshi.domain.model.NoshiTemplate

data class ExpertUiState(
    val templates: List<NoshiTemplate> = NoshiTemplate.all,
    val navigateToOmoteGaki: String? = null
)
