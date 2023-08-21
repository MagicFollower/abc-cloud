import CryptoJS from 'crypto-js';

export function sha512Hex(plainText) {
  return CryptoJS.SHA512(plainText).toString();
}
export function sha512Base64(plainText) {
  return CryptoJS.SHA512(plainText).toString(CryptoJS.enc.Base64);
}