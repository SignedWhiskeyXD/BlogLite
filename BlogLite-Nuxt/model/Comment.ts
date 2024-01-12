import type PageInfo from "~/model/PageInfo";

export default interface Comment {
    id: number;
    nickname: string;
    email: string;
    content: string;
    publish_time: string;
}

export type CommentPage = PageInfo<Comment>;