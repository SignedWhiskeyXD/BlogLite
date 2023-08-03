import {makeRequest} from "@/fetch/FetchTemplate";

export async function getBlogTags(pageNum, pageSize){
    const responsePayload = await makeRequest('/api/blogtag?pageNum=' + pageNum +"&pageSize=" + pageSize)
    if(responsePayload.code === 200)
        return responsePayload.body
    else
        return null
}

export async function removeBlogTagByID(id){
    const responseJSON = await makeRequest('/api/blogtag/' + id, {
        method: 'DELETE'
    })
    return responseJSON.code === 200
}

export async function renameBlogTagByID(id, newName){
    const responseJSON = await makeRequest('/api/blogtag?renamedID=' + id + "&newName=" + newName,{
        method: 'POST'
    })
    return responseJSON.code === 200
}

export async function addTag(tagName){
    const responseJSON = await makeRequest('/api/blogtag?tagName=' + tagName, {
        method: 'PUT'
    })
    return responseJSON.code === 200
}