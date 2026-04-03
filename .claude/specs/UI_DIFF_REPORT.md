# iOS vs Android UI実装 差分レポート

**作成日**: 2026-04-03  
**担当**: UI/UX Designer  
**目的**: iOS版のUI実装をAndroid版と比較し、差分を詳細にレポート

---

## 1. テーマ・色設定の差分

### 1.1 iOS版（NoshiTheme.swift）
**デザイン哲学**: 日本の伝統色を使用した和風デザイン

**カラーパレット**:
- **Primary**: 茶色 (#8B4513)
- **Accent**: 紅色 (#DC143C)
- **Gold**: 金色 (#D4AF37)
- **Success**: 深緑 (#228B22)
- **Warning**: 橙色 (#FF8C00)
- **Error**: 深紅 (#B22222)

**ダークモード対応**: `Color("Background")`, `Color("Surface")` などの Asset Catalog での管理

**Spacing定義**:
```
spacingXS: 4, spacingSM: 8, spacingMD: 16, spacingLG: 24, spacingXL: 32, spacingXXL: 40
```

**CornerRadius定義**:
```
radiusSM: 8, radiusMD: 12, radiusLG: 16, radiusXL: 20
```

**Shadow定義**:
```
shadowLight: 0.06, shadowMedium: 0.12, shadowStrong: 0.18 (黒色の不透明度)
```

### 1.2 Android版（Color.kt, Theme.kt）
**問題**: Material 3カラースキームへの完全な従順

**カラーパレット**:
- **Primary**: NoshiBrown (#8B4513) ✅ 一致
- **Secondary**: NoshiRed (#DC143C) ✅ 一致
- **Tertiary**: NoshiGold (#D4AF37) ✅ 一致
- **背景色**: #FFFBFF (Material 3のデフォルト色)
- **Surface**: #FFFBFF (Material 3のデフォルト色)

**ダークモード対応**: ✅ 実装されている

**問題点**:
1. ✅ Material 3の色体系は正しく定義されている
2. 🔴 Spacing定数が Color.kt, Theme.kt に定義されていない → `dp`値がコンポーネント内で直書きされている（例: 24.dp, 16.dp）
3. 🔴 CornerRadius定数がない
4. 🔴 Shadow定義がない

### 1.3 修正指示（Android）

**Spacing Constants定義**:
```kotlin
// app/src/main/java/jp/marginalgains/fastnoshi/ui/theme/Spacing.kt
package jp.marginalgains.fastnoshi.ui.theme

import androidx.compose.ui.unit.dp

object NoshiSpacing {
    val spacingXS = 4.dp
    val spacingSM = 8.dp
    val spacingMD = 16.dp
    val spacingLG = 24.dp
    val spacingXL = 32.dp
    val spacingXXL = 40.dp
}

object NoshiRadius {
    val radiusSM = 8.dp
    val radiusMD = 12.dp
    val radiusLG = 16.dp
    val radiusXL = 20.dp
}
```

**Shadow定義**:
```kotlin
// Color.kt に追加
val NoshiShadowLight = Color.Black.copy(alpha = 0.06f)
val NoshiShadowMedium = Color.Black.copy(alpha = 0.12f)
val NoshiShadowStrong = Color.Black.copy(alpha = 0.18f)
```

---

## 2. ホーム画面（NoshiHomeView.swift vs HomeScreen.kt）

### 2.1 iOS版のUIデザイン

**レイアウト構成**:
```
ScrollView {
  VStack(spacing: 24) {
    // ヘッダーセクション
    - gift.fill アイコン (高さ80, accent色)
    - タイトル「10分のし」(title スタイル, bold)
    - 説明テキスト (subheadline, 3行)
    
    // メニューセクション
    - 5つのメニュー行（各row）
      1. 「のしの作成」(wand.and.stars, accent色)
      2. 「のしの選択（自分で作成）」(square.grid.2x2, primary色)
      3. 「のしのマナー」(book.fill, gold色)
      4. 「設定」(gearshape.fill, secondary色)
      5. 「履歴」(clock.fill, success色)
  }
}
```

**各メニュー行の詳細**:
- **Icon**: 50x50のアイコン背景（色opacity: 0.12）
- **Title**: headline フォント, primary色
- **Subtitle**: caption フォント, secondary色
- **Chevron**: 右矢印アイコン (tertiary色)
- **Padding**: 各行 16dp, 外側 16dp水平

**スタイル適用**:
- CardStyle: background(surface) + shadow(light, radius:8, y:2) + cornerRadius(12)

### 2.2 Android版のUIデザイン

**レイアウト構成**:
```kotlin
Scaffold {
  Column(
    modifier: Modifier
      .fillMaxSize()
      .padding(24.dp水平, 32.dp垂直)
      .center配置,
    verticalArrangement: Arrangement.Center
  ) {
    // タイトル
    Text("10分のし", headlineLarge, primary色)
    Spacer(40.dp)
    
    // 5つのボタン（縦配置）
    NoshiPrimaryButton("のし紙を作る", onGuidedFlowClick)
    Spacer(12.dp)
    NoshiSecondaryButton("テンプレートから選ぶ", onExpertClick)
    Spacer(12.dp)
    NoshiSecondaryButton("のしマナーガイド", onMannersGuideClick)
    ...
  }
}
```

### 2.3 差分分析

| 要素 | iOS版 | Android版 | 差分 |
|------|------|---------|------|
| **レイアウト** | ScrollView内のメニュー行（Icon+Text） | ボタンリスト（中央配置） | 🔴 大きく異なる |
| **ヘッダー** | large gift アイコン + 説明文 | なし | 🔴 ヘッダー不足 |
| **メニュースタイル** | カード型（Icon背景, 多色） | ボタン型（統一色） | 🔴 ビジュアルが異なる |
| **アイコン** | 各メニューで異なる色（accent, primary, gold等） | 色分けなし | 🔴 視覚的階層構造が失われている |
| **配置** | 上詰め, スクロール可能 | 中央配置, スクロール不可 | 🔴 レイアウト戦略が異なる |

### 2.4 修正指示（Android Compose）

**HomeScreen.kt の全面リファクタリング**:

```kotlin
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
                .padding(horizontal = NoshiSpacing.spacingMD, vertical = NoshiSpacing.spacingMD),
            verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingLG)
        ) {
            // ヘッダーセクション
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingSM)
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_input_add), // またはカスタムDrawable
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(bottom = NoshiSpacing.spacingSM),
                    tint = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "10分のし",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "セブン‐イレブンで簡単のし紙印刷",
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
            .padding(top = NoshiSpacing.spacingLG, bottom = NoshiSpacing.spacingSM)

            // メニューセクション
            Column(
                verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingMD)
            ) {
                MenuRow(
                    icon = Icons.Default.AutoAwesome, // wand.and.stars相当
                    iconColor = MaterialTheme.colorScheme.secondary,
                    title = "のしの作成",
                    subtitle = "かんたんな質問に答えて、自動選択",
                    onClick = onGuidedFlowClick
                )
                MenuRow(
                    icon = Icons.Default.GridView, // square.grid.2x2相当
                    iconColor = MaterialTheme.colorScheme.primary,
                    title = "のしの選択（自分で作成）",
                    subtitle = "テンプレートから直接選択",
                    onClick = onExpertClick
                )
                MenuRow(
                    icon = Icons.Default.Book,
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
                    iconColor = MaterialTheme.colorScheme.primary, // success相当
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
    icon: androidx.compose.material.icons.Icons,
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
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp // shadow light相当
        )
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
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
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
```

---

## 3. ガイド付きフロー画面（GuidedFlowView.swift vs GuidedFlowScreen.kt）

### 3.1 iOS版のUIデザイン

**トップレイアウト**:
```
VStack(spacing: 0) {
  // プログレスバー
  GeometryReader {
    ZStack(alignment: .leading) {
      Rectangle().fill(border).frame(height: 4)  // 背景
      Rectangle().fill(accent).frame(width: 計算) // プログレス
    }
  }
  .frame(height: 4)
  
  // ステップ表示
  Text("ステップ X / 4").caption
  
  // 質問エリア
  ScrollView {
    VStack(spacing: 32) {
      // 質問アイコン (48x48)
      // 質問テキスト (title2, bold)
      // 選択肢ボタン
    }
  }
}
```

**プログレスバー**:
- **背景**: border色, 高さ4dp
- **進捗**: accent色 (DC143C), 高さ4dp
- **アニメーション**: easeInOut(0.3s)

**質問アイコン**:
- **Purpose**: gift.fill
- **CelebrationRepeat**: repeat.circle.fill
- **MarriageCheck**: heart.fill
- **CondolenceType**: leaf.fill
- **VisitType**: cross.case.fill
- **OmoteGakiSelection**: doc.text.fill

**選択肢ボタン**:
- **背景**: surface色
- **ボーダー**: border色 (1px)
- **コーナー**: radiusMD (12)
- **Shadow**: shadowLight (radius:4, y:2)
- **Padding**: 各方向 LG (24)
- **アニメーション**: opacity + scale, 遅延 index*0.05s

**表書き選択画面**:
- **ボタンスタイル**: OmoteGakiButtonStyle
- **選択時**: accent.opacity(0.1) background, accent border (2px)
- **未選択時**: surface background, border色 (1px)
- **次へボタン**: accentカラー, 下からのトランジション

### 3.2 Android版のUIデザイン

**トップレイアウト**:
```kotlin
Scaffold(
    topBar = {
        NoshiTopBar(title = "のし紙を作る", onBackClick)
    }
) { innerPadding ->
    Column(
        modifier: Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment: Alignment.CenterHorizontally
    ) {
        StepIndicator(totalSteps = 4, currentStep = uiState.stepIndex)
        Spacer(32.dp)
        AnimatedContent(targetState = uiState.currentStep) { step ->
            Column(
                horizontalAlignment: Alignment.CenterHorizontally,
                verticalArrangement: Arrangement.spacedBy(12.dp)
            ) {
                Text(uiState.questionText, headlineMedium)
                Spacer(16.dp)
                if (step == OmoteGakiSelection) {
                    OmoteGakiSelection(candidates)
                } else {
                    ChoicesList(choices)
                }
            }
        }
    }
}
```

### 3.3 差分分析

| 要素 | iOS版 | Android版 | 差分 |
|------|------|---------|------|
| **プログレス表示** | プログレスバー + テキスト | StepIndicator（未見） | ❓ 実装方式不明 |
| **トップバー** | navigationTitle | NoshiTopBar | ✅ 一致 |
| **戻るボタン** | 左矢印, 条件分岐 | NoshiTopBar内 | ✅ 一致 |
| **質問アイコン** | 大きい(48x48), contextに応じた色 | なし | 🔴 アイコン不足 |
| **アニメーション** | opacity + scale, 遅延あり | AnimatedContent | 🟡 異なるメカニズム |
| **選択肢ボタン** | surface bg, border | NoshiChoiceButton（未見） | ❓ 実装詳細不明 |

### 3.4 修正指示（Android）

**StepIndicator の改善**:
```kotlin
// NoshiTopBar直下にProgressBar + StepText を追加
Column(
    modifier = Modifier.fillMaxWidth()
) {
    LinearProgressIndicator(
        progress = { currentStep.toFloat() / totalSteps },
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp),
        color = MaterialTheme.colorScheme.secondary,
        trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
        drawStopIndicator = {}
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = NoshiSpacing.spacingLG, vertical = NoshiSpacing.spacingSM),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "ステップ $currentStep / $totalSteps",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}
```

**質問アイコンの追加**:
```kotlin
AnimatedContent(targetState = uiState.currentStep) { step ->
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingXL)
    ) {
        Icon(
            imageVector = when (step) {
                GuidedFlowStep.PURPOSE -> Icons.Default.CardGiftcard
                GuidedFlowStep.CELEBRATION_REPEAT -> Icons.Default.Repeat
                GuidedFlowStep.MARRIAGE_CHECK -> Icons.Default.Favorite
                GuidedFlowStep.CONDOLENCE_TYPE -> Icons.Default.Eco
                GuidedFlowStep.VISIT_TYPE -> Icons.Default.MedicalInformation
                GuidedFlowStep.OMOTE_GAKI_SELECTION -> Icons.Default.Description
            },
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = uiState.questionText,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(NoshiSpacing.spacingXL))
        // choices...
    }
}
```

**選択肢ボタンのアニメーション**:
```kotlin
// NoshiChoiceButton内で遅延アニメーション実装
choices.forEachIndexed { index, choice ->
    val animationDelay = index * 50
    NoshiChoiceButton(
        text = choice.label,
        onClick = { onSelected(index) },
        modifier = Modifier
            .animateContentSize()
            .graphicsLayer {
                alpha = 0.95f
            }
    )
}
```

---

## 4. テキスト入力画面（TextInputView.swift vs TextInputScreen.kt）

### 4.1 iOS版のUIデザイン

**セクション構成**:
```
ScrollView {
  VStack(spacing: 32) {
    // 表書きセクション
    VStack(alignment: .leading, spacing: 16) {
      HStack {
        Text("表書き", headline)
        Text("必須").badge(accent bg)
      }
      TextField(placeholder: "例: 御祝")
        .textFieldStyle(NoshiTextFieldStyle)
      Text("ヒント", caption, secondary)
    }
    .cardStyle()
    
    // 名前セクション
    VStack(alignment: .leading, spacing: 16) {
      HStack {
        Text("贈り主の名前", headline)
        Text("必須").badge(accent bg)
        Spacer()
        Text("X / 5名", caption)
      }
      // 名前入力フィールド
      ForEach(names.enumerated()) { index, name in
        HStack {
          Text("\(index+1)", caption)
          TextField("名前を入力")
          if count > 1 {
            Button { ... } minus.circle.fill (error)
          }
        }
      }
      Button { ... } plus.circle.fill (accent)
      Text("ヒント", caption, secondary)
    }
    .cardStyle()
    
    // プレビューボタン
    Button("プレビュー")
      .buttonStyle(PrimaryButtonStyle(color: accent))
  }
}
```

**テキストフィールドスタイル** (NoshiTextFieldStyle):
- **背景**: surface色
- **ボーダー**: border色 (1px)
- **コーナー**: radiusSM (8)
- **Padding**: spacingMD (16)

**バリデーション**:
- 必須フィールドが空でない場合のみボタン有効
- エラーメッセージは animate(top + opacity)

### 4.2 Android版のUIデザイン

**セクション構成**:
```kotlin
Column(
    verticalArrangement = Arrangement.spacedBy(16.dp)
) {
    OmoteGakiSection(
        omoteGaki = uiState.omoteGaki,
        onChanged = viewModel::onOmoteGakiChanged
    )
    NameSection(
        names = uiState.names,
        canAddName = uiState.canAddName,
        onNameChanged = viewModel::onNameChanged,
        onAddName = viewModel::onAddName,
        onRemoveName = viewModel::onRemoveName
    )
    Spacer(8.dp)
    NoshiPrimaryButton(
        text = "プレビュー",
        onClick = viewModel::onProceed,
        enabled = uiState.canProceed
    )
}

// OmoteGakiSection
Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
    Row {
        Text("表書き", titleMedium)
        Spacer(8.dp)
        Text("必須", labelSmall, error)
    }
    OutlinedTextField(
        value = omoteGaki,
        placeholder = { Text("例: 御祝") },
        singleLine = true
    )
    Text("ヒント", bodySmall, onSurfaceVariant)
}

// NameSection
Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
    Row {
        Text("贈り主の名前", titleMedium)
        Spacer(8.dp)
        Text("必須", labelSmall, error)
        Spacer(weight(1f))
        Text("${names.size} / 5名", bodySmall, onSurfaceVariant)
    }
    names.forEachIndexed { index, name ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("${index+1}", bodySmall).width(20.dp)
            OutlinedTextField(...)
            if (names.size > 1) {
                IconButton { Icon(RemoveCircle, tint: error) }
            }
        }
    }
    if (canAddName) {
        IconButton { Icon(Add, tint: primary) }
    }
}
```

### 4.3 差分分析

| 要素 | iOS版 | Android版 | 差分 |
|------|------|---------|------|
| **カード背景** | CardStyle適用 | カード背景なし | 🔴 ビジュアル異なる |
| **必須バッジ** | accent背景の小さいバッジ | Text(error)のみ | 🔴 ビジュアル異なる |
| **テキストフィールド** | NoshiTextFieldStyle (border + shadow) | OutlinedTextField (標準) | 🟡 標準の方がMaterial 3準拠 |
| **削除ボタン** | minus.circle.fill (error) | RemoveCircle icon (error) | ✅ 一致 |
| **追加ボタン** | plus.circle.fill (accent) | Add icon (primary) | 🟡 色が異なる |
| **エラー表示** | transition(top + opacity) | 未見 | ❓ 実装詳細不明 |

### 4.4 修正指示（Android）

**TextInputScreen.kt の改善**:

```kotlin
@Composable
private fun OmoteGakiSection(omoteGaki: String, onChanged: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
            verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingMD)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "表書き",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(NoshiSpacing.spacingSM))
                Badge(
                    modifier = Modifier.padding(NoshiSpacing.spacingSM, 2.dp),
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.White
                ) {
                    Text(
                        text = "必須",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            OutlinedTextField(
                value = omoteGaki,
                onValueChange = onChanged,
                placeholder = { Text("例: 御祝") },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(NoshiRadius.radiusSM)
                    ),
                singleLine = true,
                shape = RoundedCornerShape(NoshiRadius.radiusSM)
            )

            Text(
                text = "のし紙の上部に表示される用途を示すテキストです",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun NameSection(
    names: List<String>,
    canAddName: Boolean,
    onNameChanged: (Int, String) -> Unit,
    onAddName: () -> Unit,
    onRemoveName: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
            verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingMD)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "贈り主の名前",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(NoshiSpacing.spacingSM))
                Badge(
                    modifier = Modifier.padding(NoshiSpacing.spacingSM, 2.dp),
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.White
                ) {
                    Text(
                        text = "必須",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${names.size} / 5名",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (names.size > 5)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            names.forEachIndexed { index, name ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = NoshiSpacing.spacingSM),
                    horizontalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingSM)
                ) {
                    Text(
                        text = "${index + 1}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.width(20.dp)
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = { onNameChanged(index, it) },
                        placeholder = { Text("名前を入力") },
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(NoshiRadius.radiusSM)
                            ),
                        singleLine = true,
                        shape = RoundedCornerShape(NoshiRadius.radiusSM)
                    )

                    if (names.size > 1) {
                        IconButton(
                            onClick = { onRemoveName(index) },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.RemoveCircle,
                                contentDescription = "削除",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }

            if (canAddName) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onAddName() }
                        .padding(NoshiSpacing.spacingSM)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "名前を追加",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(NoshiSpacing.spacingSM))
                    Text(
                        text = "名前を追加",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Text(
                text = "のし紙の下部に表示される贈り主の名前です（受取人ではありません）",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
```

---

## 5. プレビュー画面（NoshiPreviewView.swift vs PreviewScreen.kt）

### 5.1 iOS版のUIデザイン

**要素**:
1. **Noshi Preview** (高さ280, tap to zoom機能)
2. **Paper Size Section** (A4, B4, A3のボタン)
3. **Font Selector** (Mincho, Gothic等)
4. **Font Size Section** (slider: Omote Gaki, Names)
5. **Position Section** (offset sliders)
6. **Reservation Button** または **Reservation Number Section**

**スタイル**:
- ScrollView内での縦スタック
- 各セクションはCardStyle適用
- スライダーはaccentカラー

### 5.2 Android版のUIデザイン

**要素**:
1. **Preview Image** (Bitmap from renderer)
2. **Paper Size Selection** (FilterChip)
3. **Font Selection** (FilterChip)
4. **Font Size Sliders**
5. **Primary Button** (送信)

**スタイル**:
- Material 3のFilterChip
- Slider (Material 3)

### 5.3 差分分析

| 要素 | iOS版 | Android版 | 差分 |
|------|------|---------|------|
| **プレビュー** | tap to zoom | static | 🔴 zoomなし |
| **用紙サイズ** | ボタン | FilterChip | 🟡 同等の機能 |
| **フォント選択** | picker | FilterChip | 🟡 同等の機能 |
| **Offset controls** | slider | あり | ✅ 一致 |

---

## 6. テンプレート選択画面（TemplateSelectionView.swift vs TemplateSelectionScreen.kt）

### 6.1 iOS版のUIデザイン

**レイアウト**:
```
ScrollView {
  VStack(spacing: 24) {
    // 説明セクション
    InstructionCard:
      HStack {
        Icon(graduationcap.fill, accent色, 50x50 bg)
        Text("上級者モード", headline)
        Text("説明文", caption)
      }
      .cardStyle()
    
    // テンプレートグリッド
    LazyVGrid(columns: 2) {
      ForEach(templates) { template in
        TemplateCard {
          Image(pngPreview) // アスペクト比 1:1.4
          Text(name, subheadline)
          Text(description, caption)
        }
        .cardStyle()
      }
    }
  }
}
```

**TemplateCard**:
- PNG画像プレビュー (アスペクト比 1:1.4, cornerRadius: 8)
- テンプレート名 (subheadline)
- テンプレート説明 (caption)
- CardStyle適用

### 6.2 Android版のUIデザイン

**レイアウト**:
```kotlin
LazyVerticalGrid(
    columns = GridCells.Fixed(2),
    contentPadding = PaddingValues(4.dp),
    horizontalArrangement = Arrangement.spacedBy(12.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
) {
    items(templates) { template ->
        Card(onClick = { ... }) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                padding = 12.dp
            ) {
                Text(name, titleSmall, center)
                Text(description, bodySmall, center)
            }
        }
    }
}
```

### 6.3 差分分析

| 要素 | iOS版 | Android版 | 差分 |
|------|------|---------|------|
| **説明セクション** | アイコン + テキスト, CardStyle | テキストのみ | 🔴 説明セクション不足 |
| **PNG画像プレビュー** | あり (1:1.4 aspect) | なし | 🔴 ビジュアルプレビュー不足 |
| **グリッドレイアウト** | 2列, spacing: 16 | 2列, spacing: 12 | ✅ 一致 |

### 6.4 修正指示（Android）

**TemplateSelectionScreen.kt の改善**:

```kotlin
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
                .padding(horizontal = NoshiSpacing.spacingLG, vertical = NoshiSpacing.spacingMD)
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
                            imageVector = Icons.Default.School, // graduationcap.fill相当
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.12f),
                                    shape = RoundedCornerShape(NoshiRadius.radiusSM)
                                )
                                .padding(12.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "上級者モード",
                                style = MaterialTheme.typography.headlineSmall,
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

            // テンプレートグリッド
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(0.dp),
                horizontalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingMD),
                verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingMD)
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
        modifier = Modifier.fillMaxWidth(),
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
            // PNG画像プレビュー (1:1.4 aspect ratio)
            if (template.pngPath != null) {
                Image(
                    painter = rememberAsyncImagePainter(template.pngPath),
                    contentDescription = template.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.4f)
                        .clip(RoundedCornerShape(NoshiRadius.radiusSM))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.4f)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(NoshiRadius.radiusSM)
                        ),
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
```

---

## 7. プリント画面（NoshiPrintView.swift vs PrintScreen.kt）

### 7.1 iOS版のUIデザイン

**レイアウト**:
```
ScrollView {
  VStack(spacing: 24) {
    // 説明セクション
    Text("セブン‐イレブンで印刷", headline)
    Text("説明文", subheadline)
    
    // 用紙サイズ選択
    
    // PDFプレビュー
    
    // 料金表示
    
    // 印刷ボタン または 予約番号
    
    // エラー表示
  }
}
```

### 7.2 Android版のUIデザイン

**レイアウト**:
```kotlin
Column(
    verticalArrangement = Arrangement.spacedBy(16.dp)
) {
    // InstructionSection
    // PaperSizeSection
    // PriceSection
    // PrintButton or ReservationSection
}
```

### 7.3 差分分析

| 要素 | iOS版 | Android版 | 差分 |
|------|------|---------|------|
| **全体構成** | ScrollView内のVStack | Column + verticalScroll | ✅ 一致 |
| **レイアウト** | spacing: 24 | spacing: 16 | 🟡 微差 |

---

## 8. マナーガイド画面（MannersGuideView.swift vs MannersGuideScreen.kt）

### 8.1 iOS版のUIデザイン

**レイアウト**:
```
ScrollView {
  VStack(spacing: 32) {
    // ヘッダー
    Icon(book.fill, 48, accent)
    Text("のしのマナーガイド", title2, bold)
    Text("説明", subheadline)
    
    // 内熨斗と外熨斗
    GuideCard(title, icon, description) x2
    
    // 印刷サイズガイダンス
    // 名前セクション説明
    // 贈り物選びアドバイス
  }
}
```

**GuideCard**:
- **背景**: CardStyle
- **アイコン**: 40x40程度, 色分け
- **タイトル**: subheadline
- **説明**: body

### 8.2 Android版のUIデザイン

**レイアウト**:
```kotlin
Column(
    verticalArrangement = Arrangement.spacedBy(16.dp)
) {
    GuideIntroSection()
    
    NoshiTemplate.all.forEach { template ->
        TemplateGuideCard(template)
    }
    
    NameWritingGuideSection()
}

// GuideIntroSection
Card(colors: primaryContainer) {
    Text("のし紙の選び方", titleMedium, onPrimaryContainer)
    Text("説明", bodyMedium, onPrimaryContainer)
}

// TemplateGuideCard
Card(colors: surface) {
    Text(template.name, titleSmall, primary)
    Text(template.description, bodyMedium)
    Text("印刷: ${color}（¥${price}）", bodySmall)
}
```

### 8.3 差分分析

| 要素 | iOS版 | Android版 | 差分 |
|------|------|---------|------|
| **ヘッダー** | アイコン + テキスト | なし | 🔴 ヘッダー不足 |
| **Guide Card** | CardStyle, カスタム | Material3 Card | ✅ 一致 |
| **レイアウト** | spacing: 32 | spacing: 16 | 🟡 微差 |

---

## 9. 履歴画面（NoshiHistoryView.swift vs HistoryScreen.kt）

### 9.1 iOS版のUIデザイン

**レイアウト**:
```
List {
  Section("未印刷") {
    ForEach(unprintedPapers) {
      NavigationLink {
        NoshiHistoryRow(paper)
      }
    }
    .onDelete(perform)
  }
  
  Section("印刷済み") {
    ForEach(printedPapers) {
      NavigationLink {
        NoshiHistoryRow(paper)
      }
    }
    .onDelete(perform)
  }
}
.listStyle(.insetGrouped)
```

### 9.2 Android版のUIデザイン

**レイアウト**:
```kotlin
Scaffold(topBar: ...) {
    if (isEmpty) {
        EmptyHistoryContent()
    } else {
        HistoryListContent(
            onPaperClick,
            onDeletePaper
        )
    }
}

// HistoryListContent
LazyColumn {
    items(papers) { paper ->
        SwipeToDismissBox(...) {
            HistoryItem(paper)
        }
    }
}
```

### 9.3 差分分析

| 要素 | iOS版 | Android版 | 差分 |
|------|------|---------|------|
| **セクション分け** | 未印刷 / 印刷済み | なし（統合）| 🔴 セクション分けなし |
| **削除操作** | swipe to delete | SwipeToDismissBox | ✅ 一致 |
| **リスト操作** | insetGrouped style | LazyColumn | ✅ 基本的に一致 |

### 9.4 修正指示（Android）

**HistoryScreen.kt の改善**:

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onPaperClick: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            NoshiTopBar(title = "履歴", onBackClick = onBackClick)
        }
    ) { padding ->
        if (uiState.isEmpty) {
            EmptyHistoryContent(modifier = Modifier.padding(padding))
        } else {
            HistoryListContent(
                uiState = uiState,
                onPaperClick = onPaperClick,
                onDeletePaper = viewModel::deletePaper,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
private fun HistoryListContent(
    uiState: HistoryUiState,
    onPaperClick: (String) -> Unit,
    onDeletePaper: (NoshiPaper) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = NoshiSpacing.spacingMD),
        verticalArrangement = Arrangement.spacedBy(NoshiSpacing.spacingMD)
    ) {
        // 未印刷セクション
        if (uiState.unprintedPapers.isNotEmpty()) {
            item {
                Text(
                    text = "未印刷",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = NoshiSpacing.spacingSM)
                )
            }
            items(
                items = uiState.unprintedPapers,
                key = { it.id }
            ) { paper ->
                SwipeableHistoryItem(
                    paper = paper,
                    onPaperClick = onPaperClick,
                    onDelete = { onDeletePaper(paper) }
                )
            }
        }

        // 印刷済みセクション
        if (uiState.printedPapers.isNotEmpty()) {
            item {
                Text(
                    text = "印刷済み",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = NoshiSpacing.spacingSM)
                )
            }
            items(
                items = uiState.printedPapers,
                key = { it.id }
            ) { paper ->
                SwipeableHistoryItem(
                    paper = paper,
                    onPaperClick = onPaperClick,
                    onDelete = { onDeletePaper(paper) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(NoshiSpacing.spacingMD))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeableHistoryItem(
    paper: NoshiPaper,
    onPaperClick: (String) -> Unit,
    onDelete: () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState()

    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            onDelete()
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.error),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "削除",
                    modifier = Modifier.padding(NoshiSpacing.spacingMD),
                    tint = MaterialTheme.colorScheme.onError
                )
            }
        }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onPaperClick(paper.id) },
            shape = RoundedCornerShape(NoshiRadius.radiusMD),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(NoshiSpacing.spacingMD),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = paper.omoteGaki,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = paper.names.joinToString(", "),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatDate(paper.createdAt),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    }
}
```

---

## 10. 設定画面（NoshiSettingsView.swift vs SettingsScreen.kt）

### 10.1 iOS版のUIデザイン

**レイアウト**:
```
List {
  Section("印刷方法") {
    NavigationLink { ... }
  }
  Section("印刷について") {
    NavigationLink { ... }
    Link { ... }
    Link { ... }
  }
  Section("サポート") {
    Link { ... }
  }
  Section("アプリについて") {
    HStack { Text + Spacer + Value }
    Link { ... }
    Link { ... }
  }
}
.listStyle(.insetGrouped)
```

### 10.2 Android版のUIデザイン

**レイアウト**:
```kotlin
Column(
    verticalArrangement = Arrangement.spacedBy(16.dp)
) {
    SettingsSection(title: "デフォルトフォント") {
        NoshiFontSet.all.forEach { font ->
            RadioRow(selected: ...)
        }
    }
    
    SettingsSection(title: "デフォルト用紙サイズ") {
        listOf("A4", "B4", "A3").forEach { size ->
            RadioRow(selected: ...)
        }
    }
    
    SettingsSection(title: "アプリ情報") {
        InfoRow(label, value)
    }
}
```

### 10.3 差分分析

| 要素 | iOS版 | Android版 | 差分 |
|------|------|---------|------|
| **セクション** | insetGrouped style | Card + title | 🟡 異なるスタイル |
| **コンテンツ** | Link多め | RadioButton設定多め | 🔴 内容が異なる |
| **レイアウト** | List構造 | Column構造 | ✅ 機能的に一致 |

---

## 11. 要約と優先度別修正タスク

### 🔴 **優先度 HIGH（ビジュアルが大きく異なる）**

1. **Home Screen**
   - メニュー行のカード化（Icon背景+多色化）
   - ヘッダーセクションの追加（gift icon + 説明文）
   - スクロール可能レイアウトへの変更

2. **TextInput Screen**
   - 各セクションのCard背景化
   - 必須バッジのビジュアル改善（badge component使用）

3. **Template Selection Screen**
   - 説明セクションの追加（Icon + text card）
   - PNG画像プレビューの実装（1:1.4 aspect ratio）

4. **Spacing & Dimensions Constants**
   - NoshiSpacing.kt, NoshiRadius.kt の新規作成
   - 全Screen での定数参照への統一

### 🟡 **優先度 MEDIUM（調整が必要）**

5. **GuidedFlow Screen**
   - 質問アイコンの追加
   - プログレスバー（TextWithAnimation）の改善
   - 選択肢アニメーション（遅延付き）の実装

6. **History Screen**
   - セクション分け（未印刷 / 印刷済み）の実装
   - HistoryRow の詳細実装

7. **Manners Guide Screen**
   - ヘッダーセクションの追加

### 🟢 **優先度 LOW（細部調整）**

8. **Preview Screen**
   - Tap-to-zoom機能の検討

9. **Print Screen**
   - エラー表示アニメーション

---

## 12. 実装順序（推奨）

1. **共通基盤の整備** (1-2営業日)
   - NoshiSpacing.kt, NoshiRadius.kt, Modifiers.kt 作成
   - NoshiTopBar, NoshiButton, NoshiCard などのComposable整備

2. **Home & Template Selection** (1営業日)
   - CardStyle化、アイコン背景の実装
   - 各セクションの標準化

3. **TextInput & GuidedFlow** (1-2営業日)
   - セクションのCard化
   - 質問アイコン、プログレスバーの改善

4. **Details & Polish** (1営業日)
   - History セクション分け
   - 各種アニメーション調整

---

**報告者**: UI/UX Designer  
**最終確認日**: 2026-04-03
