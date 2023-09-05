<template>
  <div class="typing-data-container">
    <div class="typing-data-question">
      <el-row>
        什么是横向领域和垂直领域？
        <el-button type="primary" icon="el-icon-search" circle @click="getAnswer" />
        <el-button type="danger" icon="el-icon-delete" circle @click="clearAnswer" />
      </el-row>
    </div>
    <div v-show="result2">
      <el-card shadow="hover">
        {{ result2 }}
      </el-card>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TypingData',
  data() {
    return {
      typingCursor: true,
      rawData: '横向领域和垂直领域是两个在不同层面上对领域或行业的分类方式。\n' +
        '\n' +
        '横向领域（Horizontal Domain）：指的是跨越多个行业或领域的广泛应用。在横向领域中，一种解决方案、技术或产品可以适用于多个不同的行业或领域。例如，大数据分析、人工智能、云计算等技术和解决方案可以在各种行业中应用，如金融、医疗、零售等。\n' +
        '\n' +
        '垂直领域（Vertical Domain）：指的是特定的行业或领域，也称为“垂直市场”。在垂直领域中，解决方案、技术或产品专注于满足特定行业的需求和特点。例如，医疗健康领域的电子病历系统、金融领域的支付解决方案、零售领域的电子商务平台等。\n' +
        '\n' +
        '横向领域和垂直领域的划分有助于更好地理解和分类不同的市场、产品和服务。横向领域解决方案可以在多个行业中应用，具有更广泛的适用性。而垂直领域解决方案则更专注于特定行业的需求和特点，可以提供更定制化和针对性的解决方案。',
      result2: '',
      result2Timer: null
    }
  },
  methods: {
    getAnswer() {
      this.result2 = '🔍处理中...'
      // 逐个显示字符的定时器
      setTimeout(() => {
        this.result2 = ''
        const words = this.rawData.split('')
        let currentPtr = 0
        this.result2Timer = setInterval(() => {
          // 检查是否已显示完全部字符
          if (currentPtr >= words.length) {
            clearInterval(this.result2Timer)
            this.typingCursor = false
            return
          }
          this.result2 += words[currentPtr]
          currentPtr++
        }, 50)
      }, 2000)
    },
    clearAnswer() {
      this.result2 = ''
      clearInterval(this.result2Timer)
    }
  }
}
</script>

<style lang="scss" scoped>
.typing-data {
  &-container {
    margin: 30px;
    white-space: pre-line; /* pre-line、nowrap */
  }

  &-question {
    margin: 0 0 20px 20px;
  }
}
</style>
