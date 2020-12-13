# application

## 必要なもの
・やる気

・Amazon Corretto 11(JDK)
: https://docs.aws.amazon.com/ja_jp/corretto/latest/corretto-11-ug/downloads-list.html

・AWSコマンドラインインターフェース
: https://aws.amazon.com/jp/cli/

・Dcoker、Docker-Compose
: https://www.docker.com/

・Intelij
: https://www.jetbrains.com/ja-jp/idea/
<p>※上記(特にエディタ)は強制ではありませんが、開発するにはある程度JVM系の言語に知見がないと詰みます。</p>

## 参照ドキュメント
    Joobyのドキュメント:https://jooby.io/

## running(開発環境で実行)

    ./gradlew joobyRun

<p>※開発環境の設定はbuild.gradleのjoobyRunタスクである程度調整可能です。</p>

## building(デプロイ)

    ./gradlew build

<p>
"build\libs\ar-application-1.0.0-all.jar"が生成されるので<br>
これを以下のように配置してjava -jar { jarファイル名 } { 環境名 }コマンドで実行する<br>
環境名を省略した場合は開発環境扱いになります<br>
confは必ずディレクトリ名固定で、中身のapplication.conf等は条件や環境に合わせて適宜入れ替えること
</p>

```
<親ディレクトリ>
│- ar-application-1.0.0-all.jar
│
└─conf
        application.conf
        application.prod.conf
        logback.xml
```