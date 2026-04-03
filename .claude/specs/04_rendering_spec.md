# レンダリング・PDF生成仕様

## 概要
のし紙テンプレート（PDF/PNG）の上にテキスト（表書き・名前）を縦書きで合成し、
印刷用PDFを生成する。

## PDF仕様
- フォーマット: PDF/X-1a:2003（印刷安全）
- 座標系: 72 DPI (1mm = 72/25.4 pt)
- ページ方向: 横向き (landscape)

### 用紙サイズ
| サイズ | 幅 mm | 高さ mm |
|--------|-------|---------|
| A4 | 297 | 210 |
| A3 | 420 | 297 |
| B4 | 364 | 257 |

## レンダリングパイプライン

```
1. テンプレートPDF読み込み（キャッシュ付き）
2. 用紙サイズに合わせてスケーリング
3. テンプレートを背景として描画
4. 縦書きテキストを合成
   a. 表書き: 上部中央
   b. 名前: 下部右寄り、縦書き
5. PDF出力
```

## 縦書きテキストレンダリング

### iOS実装
- Core Text (CTFrame, CTLine) を使用
- 文字を1文字ずつ縦に配置
- 回転なしで垂直レイアウト

### Android実装方針
- Canvas + TextPaint で1文字ずつ描画
- または android.graphics.Path + drawTextOnPath
- 縦書き用の文字間隔調整が必要
- 句読点・小文字の位置調整

### テキスト配置
- **表書き**: のし紙上部中央、フォントサイズ調整可能
- **名前**: のし紙下部右寄り、最大5名まで横に並ぶ（各名前は縦書き）
- 位置オフセット: ユーザーがスライダーで微調整可能

## テンプレートアセット
4つのテンプレートファイル（PDF + PNG各サイズ）:

| ファイル | 説明 |
|---------|------|
| 05_cho_red_on.pdf / .png | 紅白蝶結び（のし付き）|
| 05_musu_red_off.pdf / .png | 紅白結び切り |
| 10_musu_red_on.pdf / .png | 10本結び切り |
| 05_musu_black_off.pdf / .png | 黒白結び切り |

Android では `assets/` または `res/raw/` に配置。

## デザインシステム (NoshiTheme)

### カラーパレット
| 名前 | 色コード | 用途 |
|------|---------|------|
| Brown | #8B4513 | メインカラー |
| Red | #DC143C | アクセント |
| Gold | #D4AF37 | 装飾 |

### スペーシング
XS=4, SM=8, MD=16, LG=24, XL=32, XXL=40 (dp)

### 角丸
SM=8, MD=12, LG=16, XL=20 (dp)

### ダークモード
Material 3 のダイナミックカラー対応推奨

**iOS版ソース**:
- レンダラー: `FastNoshi/Services/NoshiRenderer.swift`
- PDF生成: `FastNoshi/Services/NoshiPDFGenerator.swift`
- 縦書き: `FastNoshi/Services/VerticalTextRenderer.swift`
- テーマ: `FastNoshi/Design/NoshiTheme.swift`
