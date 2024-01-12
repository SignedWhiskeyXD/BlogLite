export default interface BlogCard {
    id: number;
    title: string;
    publishTime: string;
    views: number;
    contentAbstract: string;
    previewImage: string;
    tagNames: string[];
}