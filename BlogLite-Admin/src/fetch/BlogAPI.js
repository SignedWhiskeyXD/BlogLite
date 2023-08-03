import {makeRequest} from "@/fetch/FetchTemplate";

export async function  getBlogByID(blogID){
    const response = await makeRequest('/api/blog/' + blogID)
    if(response.code === 200)
        return response.body
    else
        return null
}

export async function getBlogs(pageNum, pageSize){
    const responsePayload = await makeRequest('/api/blog?pageNum=' + pageNum +"&pageSize=" + pageSize)
    if(responsePayload.code === 200)
        return responsePayload.body
    else
        return null
}

export async function removeBlogByID(id){
    let payload = await makeRequest('/api/admin/blog/' + id, {
        method: 'DELETE'
    })
    return payload.code === 200
}

export async function editBlog(id, blogInfo){
    let payload = await makeRequest('/api/admin/blog/' + id, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(blogInfo)
    })
    return payload.code === 200
}

export async function pushNewBlog(blogInfo){
    let payload = await makeRequest('/api/admin/blog', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(blogInfo)
    })
    return payload.code === 200
}