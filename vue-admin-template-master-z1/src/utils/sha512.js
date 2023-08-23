import CryptoJS from 'crypto-js'

// COMMONJS way to export object.
// module.exports = {
//   sha512hex(content) {
//     return CryptoJS.SHA512(content).toString()
//   },
//   sha512base64(content) {
//     return CryptoJS.SHA512(content).toString(CryptoJS.enc.Base64)
//   }
// }

// ES6 way to export object, can use multi export but must have only export default.
export function sha512hex(content) {
  return CryptoJS.SHA512(content).toString()
}

export function sha512base64(content) {
  return CryptoJS.SHA512(content).toString(CryptoJS.enc.Base64)
}
