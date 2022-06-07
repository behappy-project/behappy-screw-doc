<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-select v-model="value" placeholder="请选择数据源">
        <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.value"
            :value="item.value">
        </el-option>
      </el-select>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0" v-if="user.role === 'ROLE_ADMIN'">
      <el-popconfirm
          class="ml-5"
          confirm-button-text='确定'
          cancel-button-text='我再想想'
          icon="el-icon-info"
          icon-color="red"
          title="您确定生成这些数据库的文档吗？"
          @confirm="createBatch"
      >
        <el-button type="danger" slot="reference">生成文档 <i class="el-icon-plus"></i></el-button>
      </el-popconfirm>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="icon" label="图标" width="125" align="center">
        <template slot-scope="scope">
          <i :class="scope.row.icon"></i>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="数据库名称" align="center">
        <template slot-scope="scope">
          <a target="_blank" :href="serverIp+'/doc/'+value.toLowerCase()+'/'+scope.row.name+'.html?token='+user.token" style="white-space:nowrap"><u>{{ scope.row.name }}</u></a>
        </template>
      </el-table-column>
      <el-table-column prop="description" type="textarea" label="描述" align="center"></el-table-column>
    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import {serverIp} from "../../public/config";
export default {
  name: "Doc",
  data() {
    return {
      serverIp,
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      multipleSelection: [],
      user: JSON.parse(localStorage.getItem("user")),
      options: [],
      value: 'MYSQL'
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.post("/screw/schemas", {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        name: this.name,
        dataSourceEnum: this.value,
        role: this.user.role
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    //  获取数据源
      this.request.get('/screw/options').then(res => {
        this.options = res.data
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    createBatch() {
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/screw/context-load", {
        ids,
        dataSourceEnum: this.value
      }).then(res => {
        if (res.code === '200') {
          this.$message.success("生成文档成功")
          this.load()
        } else {
          this.$message.error("生成文档失败")
        }
      })
    },
    reset() {
      this.name = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    }
  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
</style>
