import {makeRequest} from "@/fetch/FetchTemplate";

export async function getBlogStreamInitiation(num){
    const payload = await makeRequest('/api/blogstream/init?initNum=' + num);
    return payload.body;
}

export async function getBlogStream(startID, num){
    const payload = await makeRequest('/api/blogstream?startID=' + startID + '&num=' + num);
    return payload.body;
}