const Mock = require('mockjs')

const data = Mock.mock({
  'items|30': [{
    id: '@id',
    // 10~20 words in one sentence
    title: '@sentence(10, 20)',
    // choose one data from array as status
    'status|1': ['published', 'draft', 'deleted'],
    author: 'name',
    // random datetime
    display_time: '@datetime',
    // random integer in 300~5000
    pageviews: '@integer(300, 5000)'
  }]
})

module.exports = [
  {
    url: '/vue-admin-template/table/list',
    type: 'get',
    response: config => {
      const items = data.items
      return {
        code: 20000,
        data: {
          total: items.length,
          items: items
        }
      }
    }
  }
]
