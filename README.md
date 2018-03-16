# リバーシ LINE Bot 四季

[![Build Status](https://travis-ci.org/mizo0203/shiki-line-bot.svg?branch=master)](https://travis-ci.org/mizo0203/shiki-line-bot)

本アプリケーションは「東方Project」の二次創作品です。原作者の「[上海アリス幻樂団](http://www16.big.or.jp/~zun/)」様とは一切関係ございません。本アプリケーションに関するお問い合わせは、 [@mizo0203](https://github.com/mizo0203) までお願いいたします。

## 概要

LINE Bot 四季とは、リバーシゲームを LINE のトークで対人プレイできる Chatbot です。

LINE アプリで本 LINE Bot を友だち追加し、「グループ」または「トークルーム」に招待するだけでゲームを開始できます。

### 注意事項

LINE Bot が「1対1のトーク」、「グループ」または「トークルーム」に参加している間、トーク内容は Bot 運営へ送信されます。

* LINE Bot との「1対1のトーク」では、第三者に提供できないトークをしないでください
* LINE Bot を招待した「トークルーム」では、第三者に提供できないトークをしないでください
* LINE Bot を招待した「グループ」では、 LINE Bot の招待から退会までの間、第三者に提供できないトークをしないでください

### 免責事項

本アプリケーションのご利用によって発生したあらゆる不都合に対して、我々は一切の責任を負わないものといたします。

## ストーリー

準備中…

## スクリーンショット

準備中…

## 友だち追加用の QR コード

準備中…

## 本アプリケーションで利用しているもの

### LINE Messaging API

本アプリケーションは、 [LINE Messaging API](https://developers.line.me/ja/services/messaging-api/) を使用して LINE のトークを送受信しています。

* [Java SDK for Messaging API BOT](https://github.com/line/line-bot-sdk-java)
    * [Apache License 2.0](https://github.com/line/line-bot-sdk-java/blob/master/LICENSE.txt)

また、 Java アプリケーションから API を実行するため [Java SDK for Messaging API BOT](https://github.com/line/line-bot-sdk-java) を使用しています。

### Jackson

JSON パーサーライブラリ

[Java SDK for Messaging API BOT](https://github.com/line/line-bot-sdk-java) のデータクラスオブジェクト JSON 文字列に変換するために使用しています。

* [jackson-core](https://github.com/FasterXML/jackson-core)
    * [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0)
* [jackson-databind](https://github.com/FasterXML/jackson-databind)
    * [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0)
* [jackson-annotations](https://github.com/FasterXML/jackson-annotations)
    * [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0)

### Google App Engine

本アプリケーションは [Google App Engine Java スタンダード環境](https://cloud.google.com/appengine/docs/standard/java/)で稼働しています。

[![Google App Engine](image/appengine-noborder-120x30.gif)](https://cloud.google.com/appengine/)

### Bot アイコン - 四季映姫・ヤマザナドゥ

アイコン画像準備中…

「[上海アリス幻樂団](http://www16.big.or.jp/~zun/)」様制作「東方Project」の登場キャラクター

「白黒はっきりつける程度の能力」がありますが、リバーシゲームに関する公式設定はありません。

※ゲームが引き分けになり「白黒はっきりつかない」場合がありますが、リバーシゲームの仕様です。

[![上海アリス幻樂団](image/banner.gif)](http://www16.big.or.jp/~zun/)
