export function makeRequest(relativeURI, init){
    return fetch('http://localhost:52480' + relativeURI, init);
}