import {makeRequest} from "@/fetch/FetchTemplate";

export async function getAllBlogTags(){
    const payload =  await makeRequest('/api/allTags');
    return payload.body;
}