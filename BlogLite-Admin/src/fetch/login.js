import {makeRequest} from "@/fetch/requestCommon";

export async function login(formData){
    return await makeRequest('/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
    })
}