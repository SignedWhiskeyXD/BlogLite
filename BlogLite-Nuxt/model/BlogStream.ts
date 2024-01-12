import type BlogCard from "~/model/BlogCard";

export default interface BlogStream {
    blogNum: number;
    nextRequestParam: number;
    blogList: BlogCard[];
}