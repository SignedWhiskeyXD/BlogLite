export function routeToBlogDetail(blogID: number) {
    // 考虑到SSR场景不能使用客户端路由，那么就应该直接操作BOM来跳转
    window.location.href = '/blog/' + blogID.toString();
}