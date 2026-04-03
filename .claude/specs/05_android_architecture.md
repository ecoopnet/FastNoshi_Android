# Android アーキテクチャ設計

## 技術スタック

| 領域 | 技術 |
|------|------|
| 言語 | Kotlin |
| UI | Jetpack Compose + Material 3 |
| アーキテクチャ | MVVM (ViewModel + StateFlow) |
| DI | Hilt (Dagger) |
| 非同期 | Kotlin Coroutines + Flow |
| ネットワーク | Retrofit + OkHttp |
| DB | Room |
| PDF生成 | android.graphics.pdf.PdfDocument |
| 画像 | Coil (Compose対応) |
| テスト | JUnit5 + MockK + Compose Testing |
| lint | ktlint |
| ナビゲーション | Navigation Compose |

## パッケージ構成

```
jp.marginalgains.fastnoshi/
├── di/                     # Hilt モジュール
│   ├── AppModule.kt
│   ├── NetworkModule.kt
│   └── DatabaseModule.kt
├── data/
│   ├── local/              # Room DB
│   │   ├── NoshiDatabase.kt
│   │   ├── NoshiPaperDao.kt
│   │   └── NoshiPaperEntity.kt
│   ├── remote/             # Retrofit API
│   │   ├── NpsApiService.kt
│   │   └── dto/            # API DTOs
│   └── repository/
│       ├── NoshiRepository.kt
│       └── NpsRepository.kt
├── domain/
│   ├── model/              # ドメインモデル
│   │   ├── NoshiPaper.kt
│   │   ├── NoshiTemplate.kt
│   │   ├── NoshiFontSet.kt
│   │   └── GuidedFlowStep.kt
│   └── usecase/            # ユースケース
│       ├── CreateNoshiUseCase.kt
│       ├── UploadNoshiUseCase.kt
│       └── GetHistoryUseCase.kt
├── rendering/              # レンダリングエンジン
│   ├── NoshiRenderer.kt
│   ├── NoshiPdfGenerator.kt
│   └── VerticalTextRenderer.kt
├── ui/
│   ├── theme/              # Material 3 テーマ
│   │   ├── Theme.kt
│   │   ├── Color.kt
│   │   └── Type.kt
│   ├── navigation/
│   │   └── NoshiNavGraph.kt
│   ├── home/
│   │   ├── HomeScreen.kt
│   │   └── HomeViewModel.kt
│   ├── guidedflow/
│   │   ├── GuidedFlowScreen.kt
│   │   └── GuidedFlowViewModel.kt
│   ├── expert/
│   │   ├── TemplateSelectionScreen.kt
│   │   └── ExpertOmoteGakiScreen.kt
│   ├── input/
│   │   ├── TextInputScreen.kt
│   │   └── TextInputViewModel.kt
│   ├── preview/
│   │   ├── NoshiPreviewScreen.kt
│   │   └── PreviewViewModel.kt
│   ├── print/
│   │   ├── NoshiPrintScreen.kt
│   │   └── PrintViewModel.kt
│   ├── history/
│   │   ├── HistoryScreen.kt
│   │   ├── HistoryDetailScreen.kt
│   │   └── HistoryViewModel.kt
│   ├── guide/
│   │   └── MannersGuideScreen.kt
│   └── settings/
│       ├── SettingsScreen.kt
│       └── SettingsViewModel.kt
└── util/
    ├── NoshiError.kt
    └── Extensions.kt
```

## ビルドバリアント

```kotlin
// build.gradle.kts
buildTypes {
    debug {
        buildConfigField("String", "NPS_BASE_URL", "\"<sandbox_url>\"")
        buildConfigField("String", "NPS_SERVICE_TOKEN", "\"<sandbox_token>\"")
    }
    release {
        buildConfigField("String", "NPS_BASE_URL", "\"https://fix.marginalgains.jp/nps\"")
        buildConfigField("String", "NPS_SERVICE_TOKEN", "\"<production_token>\"")
        isMinifyEnabled = true
        proguardFiles(...)
    }
}
```

## iOS→Android 対応表

| iOS | Android |
|-----|---------|
| SwiftUI View | @Composable |
| @Observable | ViewModel + StateFlow |
| SwiftData | Room |
| async/await | Coroutines (suspend) |
| actor | CoroutineScope + Dispatchers |
| URLSession | Retrofit + OkHttp |
| Core Text | Canvas + TextPaint |
| UIGraphicsPDFRenderer | PdfDocument |
| PDFKit | PdfRenderer |
| Bundle.main | assets/ or res/raw/ |
| UserDefaults | DataStore |
| @Published | StateFlow / MutableStateFlow |
| NavigationStack | NavHost + NavController |
