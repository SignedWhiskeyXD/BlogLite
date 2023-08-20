import {ElMessage} from "element-plus";

export async function makeRequest(relativeURI, init) {
    const token = window.localStorage.getItem('token');

    const headers = new Headers(init ? init.headers : {});
    if (token) {
        headers.append('Authorization', `Bearer ${token}`);
    }

    const modifiedInit = { ...init, headers };

    const response = await fetch(relativeURI, modifiedInit);
    if(response.status === 403){
        ElMessage.error('权限不足')
        return null;
    }
    else if(!response.ok){
        ElMessage.error("请求错误：" + response.status + " " + response.statusText)
        return null
    }
    return await response.json();
}