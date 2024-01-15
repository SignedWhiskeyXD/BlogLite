export default function isLoopBack(ipAddr: string): boolean {
    return ipAddr === '127.0.0.1' || ipAddr === '0:0:0:0:0:0:0:1';
}