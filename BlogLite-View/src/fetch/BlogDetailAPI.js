import {makeRequest} from "@/fetch/FetchTemplate";

export async function getBlogDetail(blogID){
    const payload = await makeRequest('/api/blog/' + blogID)
    return payload.body;
}