import type RestResponse from "~/model/RestResponse";

export function responseGuard<T>(obj: RestResponse | null): T {
    if(obj === null)
        throw createError("API Server returned null");

    const response = obj as RestResponse;
    if(response.code !== 200)
        throw createError({
            statusCode: 404,
            statusMessage: response.message
        });

    return response.body as T;
}