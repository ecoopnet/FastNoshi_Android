# データモデル仕様

## NoshiPaper（保存データ）
のし紙の作成データ。iOS版はSwiftData、Android版はRoom。

| フィールド | 型 | 説明 | 備考 |
|-----------|------|------|------|
| id | UUID | 一意識別子 | PK |
| templateID | String | テンプレートID | NoshiTemplate.id参照 |
| omoteGaki | String | 表書き | 必須 |
| names | List<String> | 名前リスト | 1〜5名 |
| fontID | String | フォントID | NoshiFontSet.id参照 |
| fontSize | Float | フォントサイズ | |
| paperSize | String | 用紙サイズ | "A4", "A3", "B4" |
| createdAt | DateTime | 作成日時 | |
| lastPrintedAt | DateTime? | 最終印刷日時 | nullable |
| pdfData | ByteArray? | 生成PDF | 外部ストレージ |
| previewImageData | ByteArray? | プレビュー画像 | 外部ストレージ |

**iOS版ソース**: `FastNoshi/Models/NoshiPaper.swift`

## NoshiTemplate（テンプレート定義）
4種類の固定テンプレート。

| ID | 名前 | 説明 | 用途 | カラー |
|----|------|------|------|--------|
| 05_cho_red_on | 紅白蝶結び（のし付き） | 繰り返すお祝い | 出産祝、入学祝等 | カラー |
| 05_musu_red_off | 紅白結び切り（のしなし） | 一度きりのお祝い | 快気祝等 | カラー |
| 10_musu_red_on | 紅白結び切り10本（のし付き） | 結婚 | 結婚祝、結婚内祝 | カラー |
| 05_musu_black_off | 黒白結び切り | 弔事 | 御霊前、御仏前等 | 白黒 |

各テンプレートの属性:
| フィールド | 型 | 説明 |
|-----------|------|------|
| id | String | 一意ID |
| name | String | 表示名 |
| description | String | 使い方説明 |
| omoteGakiCandidates | List<String> | 表書き候補リスト |
| pdfFileName | String | テンプレートPDFファイル名 |
| pngFileName | String | プレビュー画像ファイル名 |
| isColor | Boolean | カラー印刷が必要か |

### 表書き候補一覧
- **05_cho_red_on**: 御祝, 御出産祝, 御入学祝, 御新築祝, 御中元, 御歳暮 等
- **05_musu_red_off**: 快気祝, 御見舞御礼, 内祝 等
- **10_musu_red_on**: 寿, 御結婚祝, 御結婚内祝 等
- **05_musu_black_off**: 御霊前, 御仏前, 御香典, 志 等

**iOS版ソース**: `FastNoshi/Models/NoshiTemplate.swift`

## NoshiFontSet（フォント定義）

| ID | 名前 | iOS フォント | Android相当 |
|----|------|-------------|-------------|
| mincho | 明朝体 | HiraMinProN-W3 | Noto Serif JP Regular |
| mincho_bold | 明朝体（太字） | HiraMinProN-W6 | Noto Serif JP Bold |
| gothic | ゴシック体 | HiraginoSans-W4 | Noto Sans JP Regular |

**iOS版ソース**: `FastNoshi/Models/NoshiFontSet.swift`

## GuidedFlowEngine（ガイドフロー状態機械）

### ステップ定義
```
PURPOSE (用途選択)
  ├→ celebration → CELEBRATION_REPEAT (繰り返し?)
  │   ├→ yes → omoteGaki候補 (蝶結び) → OMOTE_GAKI_SELECT
  │   └→ no → MARRIAGE_CHECK (結婚?)
  │       ├→ yes → omoteGaki候補 (10本結び) → OMOTE_GAKI_SELECT
  │       └→ no → omoteGaki候補 (結び切り) → OMOTE_GAKI_SELECT
  ├→ condolence → CONDOLENCE_TYPE (弔事種別)
  │   ├→ funeral → omoteGaki候補 (黒白) → OMOTE_GAKI_SELECT
  │   └→ memorial → omoteGaki候補 (黒白) → OMOTE_GAKI_SELECT
  └→ visit → VISIT_TYPE (見舞い種別)
      ├→ hospitalized → omoteGaki候補 → OMOTE_GAKI_SELECT
      ├→ recovered → omoteGaki候補 → OMOTE_GAKI_SELECT
      └→ convalescent → omoteGaki候補 → OMOTE_GAKI_SELECT
```

### 状態管理
- currentStep: 現在のステップ
- history: 戻るための履歴スタック
- selectedTemplateID: 選択されたテンプレートID
- omoteGakiCandidates: 表書き候補リスト

**iOS版ソース**: `FastNoshi/Models/GuidedFlowEngine.swift`
