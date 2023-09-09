import {makeRequest} from "@/fetch/FetchTemplate";

export async function login(formData){
    return await makeRequest('/api/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
    })
}