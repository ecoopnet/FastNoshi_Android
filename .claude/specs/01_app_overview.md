# FastNoshi アプリ概要仕様

## アプリ名
- 表示名: 10分のし
- ID: jp.marginalgains.FastNoshi
- 概要: 7-Elevenネットプリントでのし紙を10分で作成・印刷するアプリ

## iOS版情報
- iOS 17.0+, Swift 6.0
- 外部依存ゼロ（iOS標準フレームワークのみ）
- SwiftUI + SwiftData + Core Text + Core Graphics

## 画面構成

```
NoshiHomeView (ルート)
├── GuidedFlowView (ガイド付きのし作成)
│   ├── Step 1-5: 用途に応じた質問フロー
│   └── Step 6: 表書き選択 → TextInputView
├── TemplateSelectionView (エキスパートモード)
│   └── テンプレート直接選択 → ExpertOmoteGakiView
├── MannersGuideView (のしマナーガイド)
├── NoshiHistoryView (作成履歴)
│   └── NoshiHistoryDetailView (詳細/編集)
└── NoshiSettingsView (設定)

共通フロー:
TextInputView (表書き+名前入力)
  → NoshiPreviewView (WYSIWYG プレビュー)
    → NoshiPrintView (用紙サイズ選択+アップロード)
      → 予約番号表示
```

## 主要機能

### 1. ガイド付きのし作成
- 4ステップの質問フローで自動的にテンプレート選択
- 用途: お祝い / お悔やみ / お見舞い
- サブ質問: 繰り返すお祝い? / 結婚? / 弔事種別? / 見舞い種別?
- 表書き候補の自動提示

### 2. エキスパートモード
- 4テンプレートから直接選択
- グリッド表示（1.4:1アスペクト比）

### 3. テキスト入力
- 表書き（おもてがき）: 必須、1行
- 名前: 最大5名、縦書き
- バリデーション: 空文字チェック

### 4. プレビュー
- WYSIWYGレンダリング
- フォント選択（リアルタイム変更）
- フォントサイズスライダー
- 位置調整スライダー

### 5. 印刷
- 用紙サイズ: A4 / B4 / A3
- 価格表示: ¥100(白黒) / ¥200(カラー)
- ネットプリントAPIへアップロード
- 予約番号表示 + コピー機能
- 有効期限カウントダウン（7日間）

### 6. 履歴管理
- 未印刷 / 印刷済みセクション
- スワイプ削除
- 詳細表示・編集

### 7. マナーガイド
- テンプレート使い分け解説
- 表書き選択ガイド
- 名前の書き方

## iOS版ソースマッピング
| 機能 | iOSファイル |
|------|------------|
| アプリエントリ | `FastNoshi/FastNoshiApp.swift` |
| ルート画面 | `FastNoshi/ContentView.swift` |
| ホーム | `FastNoshi/Views/Home/NoshiHomeView.swift` |
| ガイドフロー | `FastNoshi/Views/GuidedFlow/GuidedFlowView.swift` |
| テキスト入力 | `FastNoshi/Views/Input/TextInputView.swift` |
| プレビュー | `FastNoshi/Views/Preview/NoshiPreviewView.swift` |
| 印刷 | `FastNoshi/Views/Print/NoshiPrintView.swift` |
| エキスパート | `FastNoshi/Views/ExpertMode/TemplateSelectionView.swift`, `ExpertOmoteGakiView.swift` |
| マナーガイド | `FastNoshi/Views/Guide/MannersGuideView.swift` |
| 履歴 | `FastNoshi/Views/History/NoshiHistoryView.swift` |
| 設定 | `FastNoshi/Views/Settings/NoshiSettingsView.swift` |
