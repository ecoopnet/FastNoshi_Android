# NPS API 仕様書

## 概要
NetPrint Proxy Server (NPS) - Rust/Axum製のプロキシサーバー。
Android アプリ → NPS Proxy → Fujifilm NetPrint API

## 接続情報

### エンドポイント
- **Production**: `https://fix.marginalgains.jp/nps` (release ビルド)
- **Sandbox**: `https://fix.marginalgains.jp/nps-s` (debug ビルド)
- BuildConfig で切替

### iOS版固定値
- `npsUserIndex`: 3 (カラーモード制御等で使用するNPSアカウントIndex)

### 認証
全リクエストに必須ヘッダー:
```
X-App-Service-Token: <サービストークン>
X-App-Name: fastnoshi   (アプリ識別、任意)
X-App-User-Id: <ユーザーID>  (ログ用、任意)
```

## エンドポイント一覧

### 1. ヘルスチェック
`GET /api/v1/health`

レスポンス:
```json
{ "status": "healthy", "version": "1.0.0", "timestamp": "..." }
```

### 2. NPS接続確認
`POST /api/v1/nps/connectivity`

リクエスト:
```json
{ "npsUserIndex": 0 }
```

レスポンス (200):
```json
{
  "success": true,
  "status": "connected",
  "npsUserIndex": 0,
  "durationMs": 150
}
```

### 3. ファイルアップロード ★メイン機能
`POST /api/v1/nps/upload`
Content-Type: `multipart/form-data`

パラメータ:
| 名前 | 型 | 必須 | 説明 |
|------|------|------|------|
| file | Binary | ○ | PDFファイル (application/pdf) ※iOS版はPDFを送信 |
| paperSize | String | ○ | 用紙サイズコード |
| colorMode | String | | "1"=カラー, "2"=白黒 |
| fileName | String | | 元ファイル名 |
| npsUserIndex | Int | | NPSユーザーIndex (iOS版は3固定) |
| waitForCompletion | String | | "true"=サーバー側ポーリング (max 60s) |

**用紙サイズコード**:
| コード | サイズ |
|--------|--------|
| "-1" | 自動 |
| "0" | A4 (デフォルト) |
| "1" | A3 |
| "2" | B5 |
| "3" | B4 |
| "4" | L判 |
| "5" | はがき |

**サーバー側の対応Content-Type** (実装確認済み):
- image/jpeg, image/jpg, application/pdf, image/png, image/tiff, application/octet-stream
- OpenAPI仕様はJPEGのみ記載だが、実装は上記すべて受付
- iOS版はPDFを送信 (fileName: "noshi.pdf", Content-Type: application/pdf)
- Android版もPDFを送信する

**用紙サイズ制限** (実装確認済み):
- paperSize "4" (L判) および "6" (2L判) は JPEG/binary のみ対応（PDF不可）
- 本アプリはA4/A3/B4のPDFなので該当しない

レスポンス (waitForCompletion=true, 200):
```json
{
  "success": true,
  "npsUserIndex": 0,
  "durationMs": 5000,
  "sessionId": "...",
  "resultCode": 0,
  "resultCodeMessage": "処理完了",
  "printId": "12345678",
  "accessKey": "ABCD"
}
```

レスポンス (waitForCompletion=false, 202):
```json
{
  "success": true,
  "npsUserIndex": 0,
  "durationMs": 2500,
  "sessionId": "1234567890abcdef...",
  "resultCode": 0
}
```

### 4. アップロード状態確認
`POST /api/v1/nps/status`

リクエスト:
```json
{ "sessionId": "...", "npsUserIndex": 0 }
```

レスポンス (200):
```json
{
  "success": true,
  "resultCode": 0,
  "printId": "ABCD1234",
  "accessKey": "9876"
}
```

**resultCode**:
| コード | 意味 | 対応 |
|--------|------|------|
| 0 | 完了 | printId/accessKey取得 |
| 1 | 処理中 | リトライ |
| -1 | メンテナンス | 503返却 |
| 4001 | レート制限 | 429返却、60秒後リトライ |

### 5. サムネイル取得
`POST /api/v1/nps/thumbnail`

リクエスト:
```json
{ "accessKey": "...", "npsUserIndex": 0 }
```

レスポンス:
```json
{
  "success": true,
  "thumbnails": [{
    "thumbnailUrl": "https://...",
    "thumbnailPageNo": "1",
    "thumbnailColor": "1",
    "thumbnailSize": "1"
  }]
}
```

### 6. ファイル情報取得
`POST /api/v1/nps/file-info`

リクエスト:
```json
{ "accessKey": "...", "npsUserIndex": 0 }
```

レスポンス:
```json
{
  "success": true,
  "printId": "ABCD1234",
  "fileName": "noshi.pdf",
  "paperSize": "0",
  "registrationDate": "2025-12-12T10:00:00+09:00",
  "expiryDate": "2025-12-19T23:59:59+09:00",
  "pageCount": 1,
  "fileSize": 1048576
}
```

### 7. システムメッセージ
`POST /api/v1/nps/message`

リクエスト:
```json
{ "messageNo": 0, "npsUserIndex": 0 }
```

## レート制限

| エンドポイント | 上限 |
|---------------|------|
| upload | 30 req/min |
| その他 | 60 req/min |

429レスポンス時は `retryAfterSeconds` 秒待ってリトライ。

## エラーハンドリング

| HTTP Status | 意味 | 対応 |
|-------------|------|------|
| 200/202 | 成功 | 正常処理 |
| 400 | パラメータ不正 | UI側でバリデーション |
| 401 | 認証失敗 | トークン確認 |
| 429 | レート制限 | 待機してリトライ |
| 503 | メンテナンス | ユーザーに通知、リトライ不可 |
| 504 | タイムアウト | リトライ |

## ポーリング戦略

### waitForCompletion=true（推奨）
- サーバー側ポーリング: 1秒間隔、最大60回（60秒タイムアウト）
- サーバーが完了を検知したら即座にprintId/accessKeyを返す
- Android版はこちらを使用する（クライアント実装がシンプル）

### waitForCompletion=false（クライアント側ポーリング）
- iOS版の実装: 3秒間隔、最大40回（120秒タイムアウト）
- サーバー側より長いタイムアウトでフォールバック

### Android版方針
- まず waitForCompletion=true でアップロード
- 60秒以内に完了しない場合（タイムアウト）、sessionIdを使ってクライアント側ポーリングにフォールバック

**iOS版ソース**: `FastNoshi/Services/NetPrintClient.swift`
**APIサーバーソース**: `/Users/mi/Work/jp.marginalgains/namaeru-server/netprint-proxy/`
**OpenAPI仕様**: `/Users/mi/Work/jp.marginalgains/namaeru-server/netprint-proxy/openapi.yaml`
