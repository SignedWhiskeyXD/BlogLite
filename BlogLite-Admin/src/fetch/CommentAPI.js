import {makeRequest} from "@/fetch/FetchTemplate";

export async function getCommentsToReview(limit) {
    if(!limit) limit = 10;

    const payload = await makeRequest('/api/admin/comment/review?num=' + limit);
    return payload.body;
}

export async function submitReviewResult(reviewInfo){
    const payload = await makeRequest('/api/admin/comment/review', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(reviewInfo)
    });

    return payload.code === 200;
}