import {makeRequest} from "@/fetch/FetchTemplate";

export async function getCommentsByBlogID(id, pageNum){
    const payload = await makeRequest('/api/comment?id=' + id + "&pageNum=" + pageNum);
    return payload.body;
}

export async function publishComment(id, commentInfo) {
    const payload = await makeRequest('/api/comment/' + id, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(commentInfo)
    });
    return {
        code: payload.code,
        message: payload.message
    };
}

export async function removeComment(blogID, commentID){
    const query = `?blogID=${blogID}&commentID=${commentID}`;
    return await makeRequest('/api/admin/comment/delete' + query, {
        method: 'DELETE'
    })
}