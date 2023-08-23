// use getters to get state directly from different modules.
// 1.vuex => vuex.<moduleAttributeName_in_vuex_modules_attribute>.<attribute_in_state_object>
const getters = {
  sidebar: vuex => vuex.app.sidebar,
  device: vuex => vuex.app.device,
  token: vuex => vuex.user.token,
  avatar: vuex => vuex.user.avatar,
  name: vuex => vuex.user.name
}
export default getters
