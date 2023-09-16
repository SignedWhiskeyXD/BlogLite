import {makeRequest} from "@/fetch/FetchTemplate";

export async function getBlogSearchResult(keyword){
    const uri = '/api/search?keyword=' + keyword;
    const payload = await makeRequest(uri);
    return payload.body;
}