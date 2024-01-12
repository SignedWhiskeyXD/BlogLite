import type CommentInput from "~/model/CommentInput";

export function preCheckInput(commentInput: CommentInput): boolean {
    let errorMessage = "";

    if (commentInput.nickname.length === 0)
        errorMessage = '请输入昵称';
    else if (commentInput.email.length === 0)
        errorMessage = '请输入邮箱';
    else if (!commentInput.email.match("[A-Za-z0-9]+([_\\.][A-Za-z0-9]+)*@([A-Za-z0-9\\-]+\\.)+[A-Za-z]{2,6}"))
        errorMessage = '请输入合法的邮箱地址'
    else if (commentInput.content.length === 0)
        errorMessage = '评论内容不可为空';
    else if (commentInput.content.length > 200)
        errorMessage = '评论字数超过限制';

    if(errorMessage.length > 0) {
        ElMessage.error(errorMessage)
        return false;
    }
    return true;
}