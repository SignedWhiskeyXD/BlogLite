import {makeRequest} from "@/fetch/FetchTemplate";

export async function getAllBlogCollections(){
    const payload = await makeRequest('/api/collection/all')
    return payload.body;
}

export async function getAllBlogsByCollectionID(collectionID){
    const payload = await makeRequest('/api/blogstream/collection/' + collectionID);
    return payload.body;
}