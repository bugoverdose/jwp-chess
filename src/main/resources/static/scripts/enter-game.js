const buildJsonBody = (form) => {
    const gameName = form.querySelector(".game_name").innerText;
    const inputValue = form.querySelector(".password_input").value;
    return {name: gameName, password: inputValue};
}

const enterGame = async (e, form) => {
    e.preventDefault();
    const response = await fetch(form.action, {
        headers: {'Content-Type': 'application/json'}, // 디폴트: text/plain;charset=UTF-8
        method: "post",
        body: JSON.stringify(buildJsonBody(form)),
        credentials: "include",
    });
    if (!response.ok) {
        return alert(await response.text());
    }
    window.location.replace(form.action);
}

const initEnterGameForms = () => {
    const forms = document.getElementsByClassName("enter-form");
    [...forms].forEach(form => form.addEventListener("submit",
        (e) => enterGame(e, form)
    ));
}

initEnterGameForms();