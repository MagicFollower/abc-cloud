import cryptoJS from 'crypto-js';

export function sha512Hex(plainText) {
    return cryptoJS.SHA512(plainText).toString();
}
export function sha512Base64(plainText) {
    return cryptoJS.SHA512(plainText).toString(CryptoJS.enc.Base64);
}