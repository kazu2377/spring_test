# CLAUDE.md

#日本語で答えてね

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Status

完全なSpring Boot Webアプリケーションが作成されました。Thymeleafテンプレートエンジンを使用してHTMLページを表示する基本的なWebアプリケーションです。

## Commands

**アプリケーションの起動**:
```bash
mvn spring-boot:run
```

**テストの実行**:
```bash
mvn test
```

**プロジェクトのビルド**:
```bash
mvn clean package
```

**JARファイルの実行**:
```bash
java -jar target/spring-test-0.0.1-SNAPSHOT.jar
```

## Architecture

**主要コンポーネント**:
- `SpringTestApplication.java` - メインのSpring Bootアプリケーションクラス
- `HomeController.java` - ルートとAboutページのコントローラー
- `index.html` / `about.html` - Thymeleafテンプレート

**URL構成**:
- `/` - ホームページ
- `/about` - Aboutページ

**技術スタック**:
- Spring Boot 3.2.0
- Spring Web
- Thymeleaf
- Maven

**ディレクトリ構造**:
```
src/
├── main/
│   ├── java/com/example/springtest/
│   │   ├── SpringTestApplication.java
│   │   └── controller/HomeController.java
│   └── resources/
│       ├── templates/ (HTML templates)
│       ├── static/ (CSS, JS, images)
│       └── application.properties
└── test/
    └── java/com/example/springtest/
        ├── SpringTestApplicationTests.java
        └── controller/HomeControllerTest.java
```

アプリケーションは`http://localhost:8080`でアクセス可能です。
