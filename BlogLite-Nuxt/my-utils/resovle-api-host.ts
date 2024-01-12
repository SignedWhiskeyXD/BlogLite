export default function resolveAPIHost() {
    return process.server ? 'http://localhost:52480' : '/';
}