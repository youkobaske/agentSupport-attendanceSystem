document.addEventListener("DOMContentLoaded", function () {

    const clockInButton =
        document.getElementById("clockInButton");

    const clockOutButton =
        document.getElementById("clockOutButton");

    const currentDate =
        document.getElementById("currentDate");

    const currentTime =
        document.getElementById("currentTime");

    const message =
        document.getElementById("message");


    /*
     * 時計表示
     */
    function updateClock() {

        const now = new Date();

        const dateFormatter =
            new Intl.DateTimeFormat(
                "ja-JP",
                {
                    year: "numeric",
                    month: "long",
                    day: "numeric",
                    weekday: "short"
                }
            );

        const timeFormatter =
            new Intl.DateTimeFormat(
                "ja-JP",
                {
                    hour: "2-digit",
                    minute: "2-digit",
                    second: "2-digit",
                    hour12: false
                }
            );

        currentDate.textContent =
            dateFormatter.format(now);

        currentTime.textContent =
            timeFormatter.format(now);
    }

    updateClock();

    setInterval(
        updateClock,
        1000
    );


    /*
     * 出勤
     */
    clockInButton.addEventListener(
        "click",
        function () {

            executeClock(
                "/AS003/clock-in"
            );
        }
    );


    /*
     * 退勤
     */
    clockOutButton.addEventListener(
        "click",
        function () {

            executeClock(
                "/AS003/clock-out"
            );
        }
    );


    /**
     * 打刻共通処理
     */
    function executeClock(url) {

        message.textContent =
            "位置情報を取得しています...";

        if (!navigator.geolocation) {

            message.textContent =
                "このブラウザでは位置情報を取得できません。";

            return;
        }

        navigator.geolocation.getCurrentPosition(

            // 取得成功
            function (position) {

                const request = {

                    latitude:
                        position.coords.latitude,

                    longitude:
                        position.coords.longitude
                };

                fetch(
                    url,
                    {
                        method: "POST",

                        headers: {
                            "Content-Type":
                                "application/json"
                        },

                        body:
                            JSON.stringify(request)
                    }
                )
                .then(async response => {

                    const text =
                        await response.text();

                    if (!response.ok) {

                        throw new Error(text);
                    }

                    return text;
                })
                .then(text => {

                    message.textContent =
                        text;

                    /*
                     * DB更新後、
                     * 再読込して表を最新化
                     */
                    window.location.reload();
                })
                .catch(error => {

                    message.textContent =
                        error.message;
                });
            },

            // 位置取得失敗
            function () {

                message.textContent =
                    "位置情報を取得できませんでした。";
            },

            {
                enableHighAccuracy: true,
                timeout: 10000,
                maximumAge: 0
            }
        );
    }
});