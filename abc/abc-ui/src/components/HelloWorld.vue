<template>
  <div>
    <p>{{ getCount }}</p>
    <button @click="increment">Increment</button>
  </div>
</template>

<script>
export default {
  name: 'HelloWorld',
  data() {
    return {};
  },
  computed: {
    getCount() {
      return this.$store.getters.getCount;
    }
  },
  methods: {
    increment() {
      this.$store.dispatch('increment');
    }
  }
};
</script>

