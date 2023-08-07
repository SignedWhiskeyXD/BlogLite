import {makeRequest} from "@/fetch/FetchTemplate";

export async function getBlogStream(startIndex, num){
    return await makeRequest('/api/blogstream?startIndex=' + startIndex + '&num=' + num);
}