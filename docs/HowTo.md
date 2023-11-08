# Nova Direct Installer の使い方

## 警告

このツールの使用に依って生じた損害については一切責任を負いません｡  
また､ チャレンジパッド３の場合は､ 通常は､ [**MTKClientを使用した方法**](//zenn.dev/s1204it/articles/16fce85441821f) を利用し､ どうしてもPC環境が無い時のみ利用して下さい｡

## 必要なもの

- チャレンジパッド ３ / Neo / Next  
  **※初期化直後の状態のものを使用して下さい**
- microSDカード
- SDカードを読み書き可能な端末

## セットアップ

⒈ Nova Launcher のAPKをダウンロードします｡

チャレンジパッド**Neo** / **Next** ：[**7.0.57**](https://teslacoilapps.com/tesladirect/download.pl?packageName=com.teslacoilsw.launcher&versionCode=70057)  
チャレンジパッド**３**：[**6.2.19**](https://teslacoilapps.com/tesladirect/download.pl?packageName=com.teslacoilsw.launcher&versionCode=62019)

⒉ 次のリンクを開き､ ファイルとして保存します｡  
[**test_environment_info.xml**](https://smiletablabo.github.io/NovaDirectInstaller/test_environment_info.xml)

⒊ SDカードの最上層に､ `test_environment_info.xml` と `NovaLauncher_x.x.xx.apk` をコピーします｡  
※ **Nova Launcher** のAPKのファイル名は変えずにコピーしてください｡

⒋ チャレンジパッドにmicroSDカードを挿入します｡

⒌ ホーム画面の､ スタートボタンを押します｡

![](https://user-images.githubusercontent.com/52069677/164911100-959604e3-d1c9-4250-9b95-94fbb2b0de62.png)

⒍ ｢<kbd><b>わかった</b></kbd>｣ を押します｡

USB または ACアダプターが接続されていないと続行できません｡  
また､ バッテリー残量が50%未満だと続行できません｡  

⒎ 画面右下に黒文字で改造が有効になっている旨の文章が在る事を確認します｡  

ここで通常と表示が変わらない場合は､ microSDカードが認識出来ていないか､ ファイルが正しく設定されていません｡

確認出来たら､ ｢<kbd><b>設定を始める</b></kbd>｣→｢<kbd><b>近くのネットワークから接続する</b></kbd>｣ からWi-Fiに接続します｡  
接続後､ ｢<kbd><b>←もどる</b></kbd>｣→｢<kbd><b>次へ</b></kbd>｣→｢<kbd><b>次へ</b></kbd>｣と進みます｡

⒏ 正常に続行すると以下のような画面が出ます｡  

![](https://github.com/SmileTabLabo/NovaDirectInstaller/assets/52069677/1f06766d-5227-477b-b3ff-fe23d3d07c2c)
指示通り､ そのままお待ちください｡  
内部処理が終了後､ 自動的に再起動されます｡

⒐ 再起動後､ Nova Launcher の初期設定を行います｡  
チャレンジパッド３の場合､ 起動時に｢`ホームアプリを選択`｣のポップアップが出るので､ ｢<kbd><b>Nova Launcher</b></kbd>｣を選択後､ ｢<kbd><b>常時</b></kbd>｣を選択して続行します｡

---

これで簡易的な改造は完了です｡

ADB が使用可能な場合は､ 以下のコマンドを必ず実行して下さい｡
```
adb shell pm uninstall --user 0 jp.co.benesse.dcha.dchaservice
```
このコマンドを実行しないと､ 予期せぬタイミングでアプリやデータがが全て削除される可能性が有ります｡
