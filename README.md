# Android GitHub Search

## 概要
Android GitHub Searchは、GitHubのAPIを使用してGitHubリポジトリを検索するAndroidアプリケーションです。ユーザーはリポジトリの詳細（所有者、スター数、プログラミング言語など）を確認できます。

## 機能
- **ユーザー名によるリポジトリ検索**: 指定したユーザー名のリポジトリの一覧を取得します。
- **キーワードによるリポジトリ検索**: キーワードでGitHubリポジトリを検索します。
- **リポジトリ詳細表示**: 各リポジトリをタップすると、WebViewを開き、リポジトリのGitHubのページを表示します。
- **お気に入りリポジトリ**: お気に入りリストにリポジトリを保存します。

## スクリーンショット
| ユーザー名によるリポジトリ検索 | キーワードによるリポジトリ検索 | リポジトリ詳細表示 | お気に入りリポジトリ |
| :--- | :---: | ---: | ---: |
| ![user](https://github.com/Nkot117/AndroidGitHubSearch/assets/52451383/419fc9d6-af4e-4118-97fb-ab1fb6426582) |![search](https://github.com/Nkot117/AndroidGitHubSearch/assets/52451383/e97a8fe7-d7e8-41a0-ac68-a6079c27d93a) | ![favorite](https://github.com/Nkot117/AndroidGitHubSearch/assets/52451383/ee8173fa-9e3b-42c4-baf1-d57cffc4ef37) | ![webview](https://github.com/Nkot117/AndroidGitHubSearch/assets/52451383/462a2dd2-bd0b-4dd5-901e-4be84ea8e1f7) |

## 使用技術
- アーキテクチャ:MVVM + Repositoryパターン
- Kotlin
- ViewModel、LiveData
- Retrofit、Moshi
- Room
- Coil
- Hilt
- mockk
- truth
- mockwebserver
