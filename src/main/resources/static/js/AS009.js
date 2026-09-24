document.addEventListener("DOMContentLoaded", () => {

    const item =
        document.getElementById("searchItem");

    const word =
        document.getElementById("searchWord");

    const text =
        document.getElementById("searchText");

    const button =
        document.getElementById("searchButton");


    item.addEventListener("change", () => {

        const selected = item.value;

        word.innerHTML =
            '<option value="">選択してください</option>';

        if (!selected) {

            word.disabled = true;
            text.disabled = true;

            return;
        }

        word.disabled = false;
        text.disabled = false;


        switch (selected) {

        case "userId":

            uniqueValues(
                adminUsers.map(u => u.userId)
            ).forEach(v => addOption(word, v, v));

            break;


        case "userName":

            uniqueValues(
                adminUsers.map(u => u.userName)
            ).forEach(v => addOption(word, v, v));

            break;


        case "actualTime":

            /*
             * 最大労働日数 × 8時間
             *
             * ここでは当月の日数ではなく
             * Java側から最大労働日数を渡す形に
             * 後で変更してもOK
             */

            addOption(
                word,
                "baseLess",
                "最大労働時間未満");

            addOption(
                word,
                "base45Less",
                "最大労働時間＋45時間未満");

            addOption(
                word,
                "base75Less",
                "最大労働時間＋75時間未満");

            addOption(
                word,
                "base75Over",
                "最大労働時間＋75時間以上");

            break;


        case "overtime":

            addOption(
                word,
                "45less",
                "45時間未満");

            addOption(
                word,
                "75less",
                "75時間未満");

            addOption(
                word,
                "75over",
                "75時間以上");

            break;


        case "paidVacation":

            addOption(word, "zero", "0日");
            addOption(word, "5less", "5日未満");
            addOption(word, "5over", "5日以上");

            break;


        case "missingClock":

            addOption(word, "5less", "5日未満");
            addOption(word, "5over", "5日以上");

            break;


        case "applyState":

            addOption(
                word,
                "01-01",
                "休暇申請承認待");

            addOption(
                word,
                "01-04",
                "休暇申請差戻し");

            addOption(
                word,
                "02-01",
                "勤怠締申請承認待");

            addOption(
                word,
                "02-04",
                "勤怠締申請差戻し");

            break;
        }
    });


    button.addEventListener("click", () => {

        const selectedItem = item.value;
        const selectedWord = word.value;
        const searchText =
            text.value.trim().toLowerCase();

        const rows =
            document.querySelectorAll(
                "#userTable tbody tr");

        rows.forEach(row => {

            let visible =
                matchesWord(
                    row,
                    selectedItem,
                    selectedWord);

            if (visible && searchText) {

                visible =
                    matchesText(
                        row,
                        selectedItem,
                        searchText);
            }

            row.style.display =
                visible ? "" : "none";
        });
    });
});


function addOption(
    select,
    value,
    label) {

    const option =
        document.createElement("option");

    option.value = value;
    option.textContent = label;

    select.appendChild(option);
}


function uniqueValues(values) {

    return [...new Set(
        values.filter(v => v != null)
    )];
}


function matchesWord(
    row,
    item,
    word) {

    if (!item || !word) {
        return true;
    }

    const d = row.dataset;

    switch (item) {

    case "userId":
        return d.userId === word;

    case "userName":
        return d.userName === word;

    case "overtime": {

        const value =
            Number(d.overtimeMinutes || 0);

        if (word === "45less")
            return value < 2700;

        if (word === "75less")
            return value >= 2700
                && value < 4500;

        if (word === "75over")
            return value >= 4500;

        return true;
    }

    case "paidVacation": {

        const value =
            Number(d.paidVacation || 0);

        if (word === "zero")
            return value === 0;

        if (word === "5less")
            return value < 5;

        if (word === "5over")
            return value >= 5;

        return true;
    }

    case "missingClock": {

        const value =
            Number(d.missingClock || 0);

        if (word === "5less")
            return value < 5;

        if (word === "5over")
            return value >= 5;

        return true;
    }

    case "applyState":

        return (
            d.applyType
            + "-"
            + d.applyState
        ) === word;
    }

    return true;
}


function matchesText(
    row,
    item,
    text) {

    const d = row.dataset;

    let value = "";

    switch (item) {

    case "userId":
        value = d.userId;
        break;

    case "userName":
        value = d.userName;
        break;

    case "actualTime":
        value = d.actualMinutes;
        break;

    case "overtime":
        value = d.overtimeMinutes;
        break;

    case "paidVacation":
        value = d.paidVacation;
        break;

    case "missingClock":
        value = d.missingClock;
        break;

    case "applyState":
        value =
            d.applyType + "-" + d.applyState;
        break;
    }

    return String(value ?? "")
        .toLowerCase()
        .includes(text);
}