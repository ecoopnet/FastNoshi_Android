# FastNoshi Android - プロジェクト規約

## プロジェクト概要
FastNoshi iOS アプリの Android 移植プロジェクト。
- iOS原本: `/Users/mi/Work/jp.marginalgains/FastNoshi`
- NPS APIサーバー: `/Users/mi/Work/jp.marginalgains/namaeru-server/netprint-proxy`
- Android: `/Users/mi/Work/jp.marginalgains/FastNoshi_Android` (本リポジトリ)

## チーム構成

| ロール | 担当 | 権限 |
|--------|------|------|
| Team Leader | チーム組成・タスク管理 | ソース参照不可。指示・タスク操作のみ |
| Devil's Advocate | 設計・実装の批判的レビュー | 全参照可 |
| Swift Engineer | iOS実装のアドバイザー | Read-only。iOS原本コードの正とする |
| Android Engineer | Android実装責任者 | 本リポジトリのみ編集可 |
| API Engineer | NPS API開発アドバイザー | Read-only。APIサーバーコードの正とする |
| UI/UX Designer | 画面設計・UX最適化 | 参照可。デザインドキュメント編集可 |

## 操作制限（厳守）
- **本 Android プロジェクトフォルダ以外の編集・操作は禁止**
- iOS / NPS API サーバーは **参照のみ許可**
- 外部サービスの情報取得・操作が必要な場合は社長（人間）に確認

## 開発方針

### テスト
- **Test First** (t-wada 方式)
  1. RED: 失敗するテストを書く
  2. GREEN: テストを通す最小実装
  3. REFACTOR: リファクタリング
- Unit Test: JUnit5 + MockK
- 統合テスト: Android UI Test (Espresso / Compose Testing)
- カバレッジ目標: 80%以上

### コーディング規約
- **Kotlin** を使用
- **ktlint** を必ず通す
- MVVM アーキテクチャ (Jetpack Compose + ViewModel)
- Kotlin Coroutines + Flow でリアクティブ
- Hilt (Dagger) で DI
- Retrofit + OkHttp で API通信
- 命名規約: Kotlin 標準 (camelCase for functions/variables, PascalCase for classes)

### コード設計原則
- John Carmack, Robert C. Martin, Rob Pike の設計思想を意識
- **重複コード禁止**: 共通化を徹底。メンテナンスでの矛盾を防ぐ
- SOLID 原則遵守
- KISS / YAGNI

### セキュリティ
- **アプリリリースを念頭に置いた開発**
- API キーやシークレットはハードコードしない
- ProGuard/R8 による難読化
- HTTPS 必須
- 入力バリデーション徹底

### ビルド設定
- **debug ビルド → sandbox 環境** (NPS API)
- **release ビルド → production 環境** (NPS API)
- BuildConfig でエンドポイント切替

### Git ワークフロー
- **main ブランチ直接作業禁止**
- 機能ごとにブランチを切る: `feature/xxx`, `fix/xxx`
- コミットメッセージ: `<type>: <description>` (feat, fix, refactor, test, docs, chore)
- 作業衝突防止のため git worktree 活用推奨
- コミットはこまめに

### 意思決定フロー
- 軽微な対応: 事後報告で可
- 大きな判断・外部サービス情報・操作が必要: 社長（人間）に確認
- 改善提案で社長確認が必要な場合のみ連絡

## 仕様管理
- iOS 実装を都度参照しなくて済むよう、仕様書に落とし込む
- iOS は随時更新されるため、**仕様書にはiOS版のソースファイルパスと該当行を併記**し、差分追跡可能にする
- 仕様書: `.claude/specs/` 配下に機能別で管理

## ディレクトリ構成規約
```
.claude/
  CLAUDE.md          - 本ファイル（プロジェクト規約）
  specs/             - 機能仕様書
  team/              - チーム関連ドキュメント
  blames.yml         - 禁止事項・指摘履歴
```
