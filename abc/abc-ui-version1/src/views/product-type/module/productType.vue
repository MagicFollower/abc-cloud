<template>
  <el-row v-loading="tableLoading" element-loading-background="rgba(255, 255, 255, 0.5)" style="height: 100%;">
    <div class="table_with_pagination">
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
          :empty-text="$t('common.tableDataEmptyText')" border style="width: 100%">
        <el-table-column
            v-for="(item, index) in column"
            :key="index"
            :prop="item.prop"
            :label="item.label"
            :width="item.width"/>
        <el-table-column :label="$t('productType.table.operate')" fixed="right" width="200">
          <template slot-scope="scope">
            <el-tooltip
                :content="!scope.row.activated ? $t('productType.table.operateConnect'): $t('productType.table.operateConnected')"
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
                :content="$t('productType.table.operateDel')"
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
            class="pagination"
            :total="total"
            :current-page="pageNum"
            background
            layout="prev, pager, next"
            @current-change="handleCurrentChange"/>
      </div>
    </div>
  </el-row>
</template>
<script>
import {mapActions} from 'vuex';
import clone from 'lodash/clone';
import API from '../api';

export default {
  name: 'RegistryCenter',
  data() {
    return {
      addDialogVisible: false,
      column: [
        {
          label: this.$t('productType').grid.code,
          prop: 'code'
        },
        {
          label: this.$t('productType').grid.name,
          prop: 'name'
        },
        {
          label: this.$t('productType').grid.memo,
          prop: 'memo'
        }
      ],
      form: {
        code: '',
        name: '',
        memo: ''
      },
      rules: {
        code: [
          {
            required: true,
            message: this.$t('productType').rules.code,
            trigger: 'change'
          }
        ],
        name: [
          {
            required: true,
            message: this.$t('productType').rules.name,
            trigger: 'change'
          }
        ],
        memo: [
          {
            required: true,
            message: this.$t('productType').rules.memo,
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
    this.getProductType();
  },
  methods: {
    // TODO 待删除
    ...mapActions(['setRegCenterActivated']),
    handleCurrentChange(val) {
      const data = clone(this.cloneTableData);
      this.tableData = data.splice((val - 1) * this.pageSize, this.pageSize);
    },
    getProductType() {
      this.tableLoading = true;
      API.queryProductType().then(res => {
        const data = res.result;
        this.total = data.length;
        this.cloneTableData = clone(data);
        this.tableData = data.splice(0, this.pageSize);
        this.tableLoading = false;
      }).catch(err => {
        this.tableLoading = false;
      });
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
