document.addEventListener("DOMContentLoaded", function () {

    const userMenuButton =
        document.getElementById("userMenuButton");

    const userMenu =
        document.getElementById("userMenu");

    const mainMenuButton =
        document.getElementById("mainMenuButton");

    const mainMenu =
        document.getElementById("mainMenu");


    // ユーザアイコン
    userMenuButton.addEventListener("click", function (event) {

        event.stopPropagation();

        userMenu.classList.toggle("active");

        // もう片方は閉じる
        mainMenu.classList.remove("active");
    });


    // 三本線
    mainMenuButton.addEventListener("click", function (event) {

        event.stopPropagation();

        mainMenu.classList.toggle("active");

        // もう片方は閉じる
        userMenu.classList.remove("active");
    });


    // メニュー以外をクリックしたら閉じる
    document.addEventListener("click", function () {

        userMenu.classList.remove("active");
        mainMenu.classList.remove("active");

    });

});