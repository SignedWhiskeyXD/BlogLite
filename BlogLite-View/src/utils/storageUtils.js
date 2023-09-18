export function saveCommentIdentity(userInfo){
    if(userInfo.nickname)
        window.localStorage.setItem('nickname', userInfo.nickname);

    if(userInfo.email)
        window.localStorage.setItem('email', userInfo.email);
}

export function getCommentIdentity() {
    let ret = {
        nickname: "",
        email: ""
    };
    ret.nickname = window.localStorage.getItem('nickname');
    ret.email = window.localStorage.getItem('email');

    return ret;
}