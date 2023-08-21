<template>
  <el-row class="box-card">
    <div class="btn-group">
      <el-button
          class="btn-plus"
          type="primary"
          icon="el-icon-plus"
          @click="add">{{ $t("btn.add") }}
      </el-button>
    </div>
    <div class="table-wrap">
      <el-table :data="tableData" border style="width: 100%">
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
        width="1010px"
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="170px">
        <el-form-item :label="$t('productInfo.addDialog.name')" prop="name">
          <el-input :placeholder="$t('productInfo.rules.name')" v-model="form.name" autocomplete="off"/>
        </el-form-item>
        <el-form-item :label="$t('productInfo.addDialog.address')" prop="zkAddressList">
          <el-input
              :placeholder="$t('productInfo.rules.address')"
              v-model="form.zkAddressList"
              autocomplete="off"
          />
        </el-form-item>
        <el-form-item :label="$t('productInfo.addDialog.namespaces')" prop="namespace">
          <el-input
              :placeholder="$t('productInfo.rules.namespaces')"
              v-model="form.namespace"
              autocomplete="off"
          />
        </el-form-item>
        <el-form-item :label="$t('productInfo.addDialog.digest')">
          <el-input
              :placeholder="$t('productInfo.rules.digest')"
              v-model="form.digest"
              autocomplete="off"
          />
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
        name: '',
        zkAddressList: '',
        namespace: '',
        digest: ''
      },
      rules: {
        name: [
          {
            required: true,
            message: this.$t('productInfo').rules.name,
            trigger: 'change'
          }
        ],
        zkAddressList: [
          {
            required: true,
            message: this.$t('productInfo').rules.address,
            trigger: 'change'
          }
        ],
        namespace: [
          {
            required: true,
            message: this.$t('productInfo').rules.namespaces,
            trigger: 'change'
          }
        ],
        instanceType: [
          {
            required: true,
            message: this.$t('productInfo').rules.centerType,
            trigger: 'change'
          }
        ],
        orchestrationName: [
          {
            required: true,
            message: this.$t('productInfo').rules.orchestrationName,
            trigger: 'change'
          }
        ]
      },
      tableData: [],
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
    ...mapActions(['setRegCenterActivated']),
    handleCurrentChange(val) {
      const data = clone(this.cloneTableData);
      this.tableData = data.splice((val - 1) * this.pageSize, this.pageSize);
    },
    getProductInfo() {
      API.queryProductInfo().then(res => {
        const data = res.result;
        this.total = data.length;
        this.cloneTableData = clone(res.model);
        this.tableData = data.splice(0, this.pageSize);
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
  margin-bottom: 20px;
}

.pagination {
  float: right;
  margin: 10px -10px 10px 0;
}
</style>
