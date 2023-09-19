function parseJWTPayload(jwt){
    const splitJWT = jwt.split('.');
    if(splitJWT.length !== 3) return {};

    const encodedPayload = splitJWT[1];
    const decodedPayload = atob(encodedPayload);

    return JSON.parse(decodedPayload);
}

export function getLoginUser() {
    const token = window.localStorage.getItem('token');
    if(!token) return null;

    const claims = parseJWTPayload(token);
    return claims.sub;
}

 export function isAdmin(){
     return getLoginUser() === "wsmrxd@gmail.com";
 }

 export function logout() {
    window.localStorage.removeItem('token');
 }