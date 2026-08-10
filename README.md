postgresql 起動方法
docker compose up -d //起動
docker ps //起動確認
docker stop [コンテナID] //停止
docker exec -it attendance-postgres bash //コンテナへ移動
psql -U attendance_user -d attendance //attendance=#へ移動
\q //終了
\dt //DB一覧

mvn spring-boot:run

1. 概要
何のためのシステムなのか
2. 解決する課題
なぜ作ったのか
3. システム構成図
4. 技術スタック
5. 機能一覧
6. DB設計
7. API一覧
8. AIエージェントの仕組み
9. 工夫したところ
10. 苦労したところ
11. 今後の改善
