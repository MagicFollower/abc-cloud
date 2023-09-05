<template>
  <div class="app-container">
    <el-input v-model="filterText" placeholder="Filter keyword" style="margin-bottom:30px;" />

    <!-- :default-expanded-keys="[data2[0].id]" -->
    <!-- default-expand-all -->
    <el-tree
      ref="tree2"
      :data="data2"
      :props="defaultProps"
      :filter-node-method="filterNode"
      class="filter-tree"
      default-expand-all
    />

    <el-button @click="getAllNodes">点我</el-button>
  </div>
</template>

<script>
export default {

  data() {
    return {
      filterText: '',
      nodes: [],
      data2: [{
        id: 1,
        title: 'Level one 1🚀',
        children: [{
          id: 4,
          title: 'Level two 1-1',
          children: [{
            id: 9,
            title: 'Level three 1-1-1'
          }, {
            id: 10,
            title: 'Level three 1-1-2'
          }]
        }]
      }, {
        id: 2,
        title: 'Level one 2🚀',
        children: [{
          id: 5,
          title: 'Level two 2-1'
        }, {
          id: 6,
          title: 'Level two 2-2'
        }]
      }, {
        id: 3,
        title: 'Level one 3🚀',
        children: [{
          id: 7,
          title: 'Level two 3-1'
        }, {
          id: 8,
          title: 'Level two 3-2'
        }]
      }],
      defaultProps: {
        children: 'children',
        label: 'title'
      }
    }
  },
  watch: {
    filterText(val) {
      this.$refs.tree2.filter(val)
    }
  },

  mounted() {
    this.$refs.tree2.store.root.childNodes.forEach(rootNode => {
      console.log(rootNode)
    })
  },

  methods: {
    filterNode(value, data) {
      if (!value) return true
      return data.title.indexOf(value) !== -1
    },
    getAllNodes() {
      this.$refs.tree2.store.root.childNodes.forEach(rootNode => {
        this.traverseNode(rootNode)
      })
      console.log(this.nodes)
    },
    traverseNode(node) {
      // node: isLeaf + childNodes + parent + data[id+title] + ...
      this.nodes.push(node)
      // !node.isLeaf
      if (node.childNodes) {
        node.childNodes.forEach(childNode => {
          this.traverseNode(childNode)
        })
      }
    }
  }
}
</script>

