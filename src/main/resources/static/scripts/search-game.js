const onSuccessResponse = ({id, found}) => {
    if (found) {
        window.location.replace(`/game/${id}`);
        return;
    }
    alert(`${id}에 해당되는 게임은 존재하지 않습니다!`)
}

const getTargetUrl = (event) => {
    const inputValue = document.getElementById("num_input").value;
    return `${event.target.action}?game_id=${inputValue}`;
}

const searchAndRedirect = async (event) => {
    event.preventDefault();
    const response = await fetch(getTargetUrl(event));
    const json = await response.json();
    onSuccessResponse(json);
}

const initSearchForm = () => {
    const form = document.getElementById("search-form");
    form.addEventListener('submit', searchAndRedirect);
}

initSearchForm();
