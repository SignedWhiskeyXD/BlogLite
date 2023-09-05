import {makeRequest} from "@/fetch/FetchTemplate";

export async function getSiteInfo(){
    const payload = await makeRequest('/api/statistic');
    return payload.body;
}

export async function getBlogRanking(){
    const payload = await makeRequest('/api/statistic/rank');
    return payload.body;
}