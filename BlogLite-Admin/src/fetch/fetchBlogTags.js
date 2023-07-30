import {makeRequest} from "@/fetch/requestCommon";

export function getBlogTags(pageNum, pageSize){
    return makeRequest('/api/blogtag?pageNum=' + pageNum +"&pageSize=" + pageSize)
        .then((response) => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json()
        })
        .then(responseData =>{
            if(responseData.code === 200)
                return responseData.responseBody
            else
                return null
        })
        .catch(reason => console.log(reason))
}

export function removeBlogTagByID(id){
    return makeRequest('/api/blogtag/' + id, {
        method: 'DELETE'
    })
}

export function renameBlogTagByID(id, newName){
    return makeRequest('/api/blogtag?renamedID=' + id + "&newName=" + newName,{
        method: 'POST'
    })
}

export function addTag(tagName){
    return makeRequest('/api/blogtag?tagName=' + tagName, {
        method: 'PUT'
    })
}