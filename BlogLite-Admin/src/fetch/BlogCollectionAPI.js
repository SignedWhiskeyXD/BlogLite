import {makeRequest} from "@/fetch/FetchTemplate";

export async function getAllBlogCollections(){
    const payload = await makeRequest('/api/collection/all')
    return payload.body;
}

export async function createNewBlogCollection(collectionInfo){
    const payload = await makeRequest('/api/admin/collection', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(collectionInfo)
    })
    return payload.code === 200
}

export async function removeBlogCollection(collectionID){
    const payload = await makeRequest('/api/admin/collection/' + collectionID, {
        method: 'DELETE',
    })
    return payload.code === 200
}

export async  function modifyBlogCollection(collectionInfo){
    const payload = await makeRequest('/api/admin/collection', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(collectionInfo)
    })
    return payload.code === 200
}

export async function getAllBlogsByCollectionID(collectionID){
    const payload = await makeRequest('/api/blogstream/collection/' + collectionID);
    return payload.body;
}