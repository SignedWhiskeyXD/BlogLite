import {makeRequest} from "@/fetch/requestCommon";

export async function getBlogs(pageNum, pageSize){
    const responsePayload = await makeRequest('/api/blog?pageNum=' + pageNum +"&pageSize=" + pageSize)
    if(responsePayload.code === 200)
        return responsePayload.responseBody
    else
        return null
}

export async function removeBlogByID(id){
    let payload = await makeRequest('/api/admin/blog/' + id, {
        method: 'DELETE'
    })
    return payload.code === 200
}

export async function pushNewBlog(title, content){
    let payload = await makeRequest('/api/admin/blog', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            title: title,
            content: content
        })
    })
    return payload.code === 200
}