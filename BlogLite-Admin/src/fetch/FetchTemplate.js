import {ElMessage} from "element-plus";
import router from "@/router";

export async function makeRequest(relativeURI, init) {
    const token = window.localStorage.getItem('token');

    // Create headers object and add Authorization header if token exists
    const headers = new Headers(init ? init.headers : {});
    if (token) {
        headers.append('Authorization', `Bearer ${token}`);
    }

    // Modify init object to include the updated headers
    const modifiedInit = { ...init, headers };

    const response = await fetch(relativeURI, modifiedInit);
    if(response.status === 401){
        await router.push('/login')
        return null
    }else if(response.status === 403){
        ElMessage.error('权限不足')
        return null;
    }
    else if(!response.ok){
        ElMessage.error("请求错误：" + response.status + " " + response.statusText)
        return null
    }
    return await response.json();
}