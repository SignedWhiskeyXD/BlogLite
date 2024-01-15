import isLoopBack from "~/my-utils/is-loopback";

export default defineEventHandler(req => {
    if(!req.path.startsWith('/blog/')) return;

    const clientHeaders = req.headers;

    // H3似乎会将所有的HTTP首部转小写
    const clientIP = clientHeaders.get('x-real-ip');
    const clientUA = clientHeaders.get('user-agent');
    const blogID = parseInt(req.path.split('/')[2]);

    if(isNaN(blogID) || clientIP == null || isLoopBack(clientIP)) return;

    $fetch('http://localhost:52480/api/blog/views', {
        method: 'POST',
        body: {
            ipAddr: clientIP,
            userAgent: clientUA,
            blogID: blogID
        }
    });
})