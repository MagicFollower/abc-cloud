<template>
  <el-row v-loading="tableLoading" element-loading-background="rgba(255, 255, 255, 0.5)" style="height: 100%;">
    <!-- 在Flex容器中的元素将竖向排列，从上到下依次显示，靠左对齐 -->
    <div class='table_with_pagination'>
      <div class="btn-group">
        <el-button
            class="btn-plus"
            type="primary"
            icon="el-icon-plus"
            @click="add">{{ $t("btn.add") }}
        </el-button>
      </div>
      <el-table
          :data="tableData"
          :empty-text="$t('common.tableDataEmptyText')" border>
        <el-table-column
            v-for="(item, index) in gridColumns"
            :key="index"
            :prop="item.prop"
            :label="item.label"
            :width="item.width"/>
        <el-table-column :label="$t('productInfo.table.operate')" fixed="right" width="200">
          <template slot-scope="scope">
            <el-tooltip
                :content="!scope.row.activated ? $t('productInfo.table.operateConnect'): $t('productInfo.table.operateConnected')"
                class="item"
                effect="dark"
                placement="top">
              <el-button
                  :type="scope.row.activated ? 'success' : 'primary'"
                  icon="el-icon-link"
                  size="small"
                  @click="handleConnect(scope.row)"/>
            </el-tooltip>
            <el-tooltip
                :content="$t('productInfo.table.operateDel')"
                class="item"
                effect="dark"
                placement="top">
              <el-button
                  size="small"
                  type="danger"
                  icon="el-icon-delete"
                  @click="handlerDel(scope.row)"/>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
            :total="total"
            :current-page="pageNum"
            background
            layout="prev, pager, next"
            @current-change="handleCurrentChange"
        />
      </div>
    </div>
    <el-dialog
        :title="$t('productInfo.addDialog.title')"
        :visible.sync="addDialogVisible"
        @open="addDialogOpen"
        @closed="addDialogClose"
        width="1010px">
      <!-- model绑定数据列表，rules绑定校验规则列表 -->
      <el-form ref="form" :model="form" :rules="rules" label-width="170px">
        <!-- prop用于规格匹配 -->
        <el-form-item :label="$t('productInfo.addDialog.typeName')" prop="typeName">
          <!-- el-select的v-model用于双向绑定待填充数据 -->
          <el-select clearable v-model="form.typeName" :placeholder="$t('productInfo.addDialog.typeName')">
            <el-option v-for="(item, index) in fromProductTypeList"
                       :key="index" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('productInfo.addDialog.code')" prop="code">
          <!-- el-input的v-model用于双向绑定待填充数据 -->
          <el-input :placeholder="$t('productInfo.rules.code')" v-model="form.code" autocomplete="off"/>
        </el-form-item>
        <el-form-item :label="$t('productInfo.addDialog.name')" prop="name">
          <el-input :placeholder="$t('productInfo.rules.name')" v-model="form.name" autocomplete="off"/>
        </el-form-item>
        <el-form-item :label="$t('productInfo.addDialog.actPrice')" prop="actPrice">
          <el-input :placeholder="$t('productInfo.rules.actPrice')" v-model="form.actPrice" autocomplete="off"/>
        </el-form-item>
        <el-form-item :label="$t('productInfo.addDialog.salePrice')" prop="salePrice">
          <el-input :placeholder="$t('productInfo.rules.salePrice')" v-model="form.salePrice" autocomplete="off"/>
        </el-form-item>
        <el-form-item :label="$t('productInfo.addDialog.memo')">
          <el-input :placeholder="$t('productInfo.rules.memo')" v-model="form.memo" autocomplete="off"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">{{ $t("productInfo.addDialog.btnCancelTxt") }}</el-button>
        <el-button
            type="primary"
            @click="onConfirm('form')"
        >{{ $t("productInfo.addDialog.btnConfirmTxt") }}
        </el-button>
      </div>
    </el-dialog>
  </el-row>
</template>
<script>
import API from '../api';
import {mapActions} from 'vuex';
import clone from 'lodash/clone';

export default {
  name: 'RegistryCenter',
  data() {
    return {
      addDialogVisible: false,
      gridColumns: [
        {
          label: this.$t('productInfo').grid.code,
          prop: 'code'
        },
        {
          label: this.$t('productInfo').grid.name,
          prop: 'name'
        },
        {
          label: this.$t('productInfo').grid.typeName,
          prop: 'typeName'
        },
        {
          label: this.$t('productInfo').grid.actPrice,
          prop: 'actPrice'
        },
        {
          label: this.$t('productInfo').grid.salePrice,
          prop: 'salePrice'
        },
        {
          label: this.$t('productInfo').grid.memo,
          prop: 'memo'
        }
      ],
      form: {
        code: '',
        name: '',
        typeName: '',
        actPrice: '',
        salePrice: '',
        memo: '',
      },
      fromProductTypeList: [],
      rules: {
        code: [
          {
            required: true,
            message: this.$t('productInfo').rules.code,
            trigger: 'change'
          }
        ],
        name: [
          {
            required: true,
            message: this.$t('productInfo').rules.name,
            trigger: 'change'
          }
        ],
        typeName: [
          {
            required: true,
            message: this.$t('productInfo').rules.typeName,
            trigger: 'change'
          }
        ],
        actPrice: [
          {
            required: true,
            message: this.$t('productInfo').rules.actPrice,
            trigger: 'change'
          }
        ],
        salePrice: [
          {
            required: true,
            message: this.$t('productInfo').rules.salePrice,
            trigger: 'change'
          }
        ],
      },
      tableData: [],
      tableLoading: false,
      cloneTableData: [],
      pageNum: 1,
      pageSize: 10,
      total: 0
    };
  },
  created() {
    this.getProductInfo();
  },
  methods: {
    // TODO 待删除
    ...mapActions(['setRegCenterActivated']),
    getProductInfo() {
      this.tableLoading = true;
      API.queryProductInfo().then(res => {
        const data = res.result;
        this.total = data.length;
        this.cloneTableData = clone(data);
        this.tableData = data.splice(0, this.pageSize);
        this.tableLoading = false;
      }).catch(err => {
        this.tableLoading = false;
      });
    },
    handleCurrentChange(val) {
      const data = clone(this.cloneTableData);
      this.tableData = data.splice((val - 1) * this.pageSize, this.pageSize);
    },
    /* 弹窗打开时，加载商品类型数据 */
    /* 弹窗关闭结束时，清空填充信息+规则提示信息 */
    addDialogOpen() {
      API.queryProductType().then(res => {
        this.fromProductTypeList = res.result;
      });
    },
    addDialogClose() {
      this.$refs.form.resetFields();
    },

    handleConnect(row) {
      if (row.activated) {
        this.$notify({
          title: this.$t('common').notify.title,
          message: this.$t('common').connected,
          type: 'success'
        });
      } else {
        // const params = {
        //   name: row.name
        // };
        // API.postRegCenterConnect(params).then(res => {
        //   this.$notify({
        //     title: this.$t('common').notify.title,
        //     message: this.$t('common').notify.conSucMessage,
        //     type: 'success'
        //   });
        //   this.getRegCenter();
        // });
      }
    },
    handlerDel(row) {
      const params = {
        name: row.name
      };
      // API.deleteRegCenter(params).then(res => {
      //   this.$notify({
      //     title: this.$t('common').notify.title,
      //     message: this.$t('common').notify.delSucMessage,
      //     type: 'success'
      //   });
      //   this.getRegCenter();
      // });
    },
    onConfirm(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          // API.postRegCenter(this.form).then(res => {
          //   this.addDialogVisible = false;
          //   this.$notify({
          //     title: this.$t('common').notify.title,
          //     message: this.$t('common').notify.addSucMessage,
          //     type: 'success'
          //   });
          //   this.getRegCenter();
          // });
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    add() {
      this.addDialogVisible = true;
    }
  }
};
</script>
<style lang='scss' scoped>
.btn-group {
  display: flex;
  margin-bottom: 10px;
}
.table_with_pagination {
  display: flex;
  flex-direction: column;
  height: 100%;
}
.pagination {
  margin: 5px -10px 0 0;
  display: flex;
  justify-content: end;
  align-items: start;
}
</style>
