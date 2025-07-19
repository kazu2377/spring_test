
## プロジェクト選定

**推奨**: **簡単なイベント予約システム**

なぜこれが学習に最適か：
- **トランザクション学習**: 定員制限で同時予約の競合状態を再現できる
- **実用性**: 実際に勉強会やミーティングの予約に使える
- **拡張性**: 後からテスト、ログ、認証機能を追加しやすい

### 基本機能
1. **イベント管理**: イベントの作成・編集・削除
2. **予約機能**: ユーザーがイベントに参加予約
3. **定員管理**: 定員を超えた予約を防ぐ（トランザクション重要）
4. **予約一覧**: 自分の予約確認

## 環境構築手順

### 1. 開発環境の準備

**必要なツール**:
```bash
# Java 17以上
java -version

# Maven または Gradle
mvn -version

# MySQL (Docker推奨)
docker --version
```

**MySQL起動**（Docker使用）:
```bash
docker run --name mysql-study \
  -e MYSQL_ROOT_PASSWORD=password \
  -e MYSQL_DATABASE=event_booking \
  -p 3306:3306 \
  -d mysql:8.0
```

### 2. Spring Bootプロジェクト作成

**Spring Initializr**で以下を選択：
- **Project**: Maven
- **Language**: Java
- **Spring Boot**: 3.2.x
- **Group**: com.study
- **Artifact**: event-booking
- **Dependencies**:
  - Spring Web
  - Spring Data JPA
  - MySQL Driver
  - Thymeleaf
  - Spring Boot DevTools

### 3. 基本的なファイル構成

```
src/main/java/com/study/eventbooking/
├── EventBookingApplication.java
├── controller/
│   ├── EventController.java
│   └── BookingController.java
├── entity/
│   ├── Event.java
│   ├── Booking.java
│   └── User.java
├── repository/
│   ├── EventRepository.java
│   ├── BookingRepository.java
│   └── UserRepository.java
├── service/
│   ├── EventService.java
│   └── BookingService.java
└── config/
    └── DatabaseConfig.java
```
