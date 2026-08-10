postgresql 起動方法
docker compose up -d //起動
docker ps //起動確認
docker stop [コンテナID] //停止
docker exec -it attendance-postgres bash //コンテナへ移動
psql -U attendance_user -d attendance //attendance=#へ移動
\q //終了
\dt //DB一覧

mvn spring-boot:run