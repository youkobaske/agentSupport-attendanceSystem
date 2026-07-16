Phase 1：業務システム
	• Spring Boot 
	• MyBatis 
	• Oracle（またはPostgreSQL） 
	• Spring Security 
	• Thymeleaf 
まずは「勤怠システム」として完成させます。
Phase 2：運用機能
	• 障害報告 
	• チケット管理 
	• 添付ファイル 
	• コメント 
	• ステータス管理 
ここで「運用者向け」の機能を追加します。
Phase 3：AIエージェント
	• Copilot連携 
	• 障害内容の要約 
	• 原因候補の提示 
	• 対応策の提案 
	• 類似障害の検索 
	• FAQ生成 
AIを業務フローの一部として組み込みます。
Phase 4：インフラ・品質
	• GitHub ActionsによるCI 
	• Dockerでの開発環境構築 
	• AWSへのデプロイ（EC2やRDSなど） 
	• テストコード（JUnit） 
	• システム構成図・ER図・画面遷移図をREADMEやdocsに整備

●設計
成果物
	• 要件定義 
	• 画面一覧 
	• 画面遷移図 
	• ER図 
	• テーブル定義 
	• API一覧

勤怠システム → Redmine → Copilot Agent → Spring Boot API

┌────────────────────────────┐
│         勤怠システム          │
│  (Spring Boot + Oracle)     │
└────────────┬───────────────┘
             │
             ▼
┌────────────────────────────┐
│       障害管理システム         │
│                              │
│ ・障害登録                    │
│ ・原因                        │
│ ・対応                        │
│ ・FAQ                         │
└────────────┬───────────────┘
             │ REST API
             ▼
┌────────────────────────────┐
│     Spring Boot API層        │
│                              │
│ ・障害検索                    │
│ ・FAQ検索                     │
│ ・ログ検索                    │
└────────────┬───────────────┘
             │
             ▼
┌────────────────────────────┐
│     Microsoft Copilot       │
│         (Agent)             │
│                              │
│ ・API呼び出し                │
│ ・プロンプト生成              │
│ ・回答生成                    │
└────────────────────────────┘

使用API
• Spring Boot 
• MyBatis 
• Oracle（またはPostgreSQL） 
• Spring Security 
• Thymeleaf

機能
	• ログイン 
	• 打刻 
	• 勤怠一覧 
	• 社員管理 
	• 管理者画面 
	• 有給申請

  ①ログイン画面
↓ユーザID,パスワード入力
②ホーム画面
（お知らせ表示、上部固定タブ：ホーム、打刻、勤怠一覧、管理者（管理者のみ）、諸々申請、ログアウト）
↓②タブ：ホーム
②ホーム画面
↓②タブ：打刻
③打刻画面
（出勤、退勤ボタン）
↓②タブ：勤怠一覧
④勤怠一覧画面
（当月の1日ごとに出勤・退勤時間、位置情報（打刻ボタン押下した瞬間の位置））
↓②タブ：申請
⑤申請画面
（ボタン：有給申請、勤怠締め申請）
↓⑤ボタン：有給申請
⑥有給申請画面
↓⑤ボタン：勤怠締め申請
⑦勤怠締め申請画面
↓②タブ：管理者
⑧管理者画面
（部内の社員一覧、リンク：社員名）
↓⑧リンク
⑨該当社員画面
（社員の勤怠状況一覧（④の画面 + 締め状況））
↓②ログアウトリンク
①ログイン画面

【管理者】
      │
      │ 障害発生
      ▼
Redmine
────────────────────────
タイトル
内容
原因
対応
コメント
添付ファイル
────────────────────────
      ▲
      │ REST API
      │
Spring Boot(API)
      ▲
      │
Copilot Agent
      ▲
      │
【ユーザー】
「打刻できません」
<img width="620" height="884" alt="image" src="https://github.com/user-attachments/assets/751fab6e-78bc-4472-92bd-6abfe41fb4b6" />

<img width="951" height="1032" alt="image" src="https://github.com/user-attachments/assets/8faf26e1-da83-4b35-b928-1c1075c83254" />

<img width="264" height="259" alt="image" src="https://github.com/user-attachments/assets/2d2eeefd-1cf1-4878-8a60-85c8ede8f887" />


<img width="427" height="222" alt="image" src="https://github.com/user-attachments/assets/5b0f22c8-d7e2-403b-a02f-290b31d4df7f" />

<img width="496" height="1621" alt="image" src="https://github.com/user-attachments/assets/4c52414a-af2e-4b8b-bd2c-bfb99193bc8c" />
