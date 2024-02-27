# Nova Direct Installer の使い方

## 警告

> [!CAUTION]
> このツールの使用に依って生じた損害については一切責任を負いません｡  
また､ チャレンジパッド３の場合は､ 通常は [**MTKClientを使用した方法**](https://zenn.dev/s1204it/articles/16fce85441821f) を利用し､ どうしてもPC環境が無い時のみ利用して下さい｡

## 必要なもの

- チャレンジパッド ３ / Neo / Next  
  **※初期化直後の状態のものを使用して下さい**
- microSDカード
- SDカードを読み書き可能な端末

## セットアップ

⒈ Nova Launcher のAPKをダウンロードします｡

チャレンジパッド**Neo** / **Next** ：[**7.0.58**](https://teslacoilapps.com/tesladirect/download.pl?packageName=com.teslacoilsw.launcher&versionCode=70058)  
チャレンジパッド**３**：[**6.2.19**](https://teslacoilapps.com/tesladirect/download.pl?packageName=com.teslacoilsw.launcher&versionCode=62019)

> [!IMPORTANT]
> **※ Next の場合は Playストア を利用できます**  
以下のZIPファイルをダウンロードし、５つのAPKを抽出してください。  
> - [**CTZ-GApps**.zip](https://subcontent.s1204.me/android/CTZ-GApps.zip)

⒉ 次のリンクを開き､ ファイルとして保存します｡  
[**test_environment_info.xml**](https://gh.s1204.me/NovaDirectInstaller/test_environment_info.xml)

⒊ SDカードの最上層に､ `test_environment_info.xml` をコピーし､ **APK** と言う名前のフォルダを作りダウンロードした APK をコピーします｡  
```
SDカードの最上層
├── APK
│   ├── GmsCore.apk
│   ├── GoogleServicesFramework.apk
│   ├── NovaLauncher_6.2.19.apk
│   ├── NovaLauncher_7.0.58.apk
│   └── Phonesky.apk
└── test_environment_info.xml
```
> [!WARNING]
> ※ APKのファイル名は変えずにコピーしてください｡  

⒋ チャレンジパッドにmicroSDカードを挿入します｡

⒌ ホーム画面の､ スタートボタンを押します｡

[![](https://github.com/s1204IT/NovaDirectInstaller/assets/52069677/7b570b1d-60b5-4186-8080-4fbdde7e6e9c)](#)

⒍ ｢<kbd><b>わかった</b></kbd>｣ を押します｡

> [!NOTE]
> USB または ACアダプターが接続されていないと続行できません｡  
また､ バッテリー残量が50%未満だと続行できません｡  

⒎ 画面右下に黒文字で改造が有効になっている旨の文章が在る事を確認します｡  

ここで通常と表示が変わらない場合は､ microSDカードが認識出来ていないか､ ファイルが正しく設定されていません｡

確認出来たら､ ｢<kbd><b>設定を始める</b></kbd>｣→｢<kbd><b>近くのネットワークから接続する</b></kbd>｣ からWi-Fiに接続します｡  
接続後､ ｢<kbd><b>←もどる</b></kbd>｣→｢<kbd><b>次へ</b></kbd>｣→｢<kbd><b>次へ</b></kbd>｣と進みます｡

⒏ 正常に続行すると以下のような画面が出ます｡

[![](https://github.com/s1204IT/NovaDirectInstaller/assets/52069677/8b883890-c60a-4043-9636-ea90e6730516)](#)
**画面には触れずに** そのままお待ちください｡  
内部処理が終了後､ 自動的に再起動されます｡

⒐ 再起動後､ Nova Launcher の初期設定を行います｡  
チャレンジパッド３の場合､ 起動時に｢`ホームアプリを選択`｣のポップアップが出るので､ ｢<kbd><b>Nova Launcher</b></kbd>｣を選択後､ ｢<kbd><b>常時</b></kbd>｣を選択して続行します｡

---

これで簡易的な改造は完了です｡

## チャレンジパッドNext の例外について

チャレンジパッドNext では **Google Playストア** が使用できます。  
> [!WARNING]
> 但し､ **DchaState** を `3` 以外にすると突然動作しなくなります。
> これの回避策として､ [**BypassRevokePermission**](https://github.com/Kobold831/BypassRevokePermission) と言うアプリを統合しています。  
> このアプリの機能を使用するには、**Play ストア** と **Google Play開発者サービス** を更新し、アプリ一覧にある「**BRPの有効化**」と言うアプリ(アクティビティ)を起動してください。

また､ `count_dcha_completed` が存在しない場合でも無視して **DchaState** を `3` にするので､  
**QSB** や AOSPの**ブラウザ**､ **パッケージインストーラー** を使用したい場合は､  
オリジナルの [**DchaState Changer**](https://github.com/SmileTabLabo/DchaStateChanger) を使用してください｡
