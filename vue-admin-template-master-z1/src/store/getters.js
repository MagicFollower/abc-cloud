// use getters to get state directly from different modules.
// 1.vuex => vuex.<moduleAttributeName_in_vuex_modules_attribute>.<attribute_in_state_object>
// 2.function value has one parameter [state] => you also can use 'this.$store.state.settings.title' in vue-component.
const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name
}
export default getters
