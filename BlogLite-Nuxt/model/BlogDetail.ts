export default interface BlogDetail {
    id: number;
    title: string;
    publishTime: string;
    views: number;
    contentHTML: string;
    tagNames: string[]
}