<template>
  <div>
    <div style="margin: 10px 0">
      <el-select v-model="value" placeholder="请选择数据源类型">
        <el-option
            v-for="(item,index) in options"
            :key="index"
            :label="item"
            :value="item">
        </el-option>
      </el-select>
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" class="ml-5"
                v-model="dsName"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="数据源名称"></el-table-column>
      <el-table-column prop="addr" label="地址"></el-table-column>
      <el-table-column prop="port" label="端口" width="80"></el-table-column>
      <el-table-column prop="ignoreTableName" type="textarea" label="忽略表名"></el-table-column>
      <el-table-column prop="ignorePrefix" type="textarea" label="忽略表前缀"></el-table-column>
      <el-table-column prop="ignoreSuffix" type="textarea" label="忽略表后缀"></el-table-column>
      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
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

    <el-dialog title="配置数据源信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="80px" size="small" :model="form" ref="form" :rules="rules">
        <el-form-item label="数据源类型">
          <el-select clearable v-model="form.dataSource" placeholder="请选择数据源类型" style="width: 100%">
            <el-option v-for="(item,index) in options" :key="index" :label="item" :value="item"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数据源名称(唯一)" prop="name">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.addr" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="端口">
          <el-input v-model="form.port" type="number" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="用户名">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" autocomplete="off" show-password></el-input>
        </el-form-item>
        <el-form-item label="忽略表名(逗号分割)">
          <el-input v-model="form.ignoreTableName" type="textarea" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="忽略表前缀(逗号分割)">
          <el-input v-model="form.ignorePrefix" type="textarea" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="忽略表后缀(逗号分割)">
          <el-input v-model="form.ignoreSuffix" type="textarea" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="danger" @click="dbCheck">测试连接</el-button>
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Datasource",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      dsName: "",
      form: {},
      dialogFormVisible: false,
      user: JSON.parse(localStorage.getItem("user")),
      options: [],
      value: 'MYSQL',
      rules: {
        name: [
          {required: true, message: '名称不能为空', trigger: "blur"},
          {pattern: /^[a-zA-Z0-9_]*$/, message: '名称不能含有中文或特殊字符', trigger: "blur"},
        ]
      },
    }
  },
  created() {
    this.load()
    this.getDatasourceType()
  },
  methods: {
    checkNameRule(value, callback) {
      console.log('规则',value)
      this.form.name = value
      const reg = /^[a-zA-Z0-9_]*$/
      if (value == '' || value == undefined || value !== value) {
        callback("名称不能为空!");
        return;
      } else if (!reg.test(value)) {
        callback("名称不能含有中文或特殊字符!");
        return;
      } else {
        callback();
        return
      }
    },
    load() {
      this.request.get("/datasource", {
        params: {
          dataSource: this.value,
          name: this.dsName,
        }
      }).then(res => {
        this.tableData = res.data
        this.total = res.data.length
      })
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    getDatasourceType() {
      //  获取数据源类型
      this.request.get('/database/options').then(res => {
        this.options = res.data
      })
    },
    save() {
      this.$refs.form.validate((valid) => {
        if (valid){
          this.request.post("/datasource", this.form).then(res => {
            if (res.code === '200') {
              this.$message.success("保存成功")
              this.dialogFormVisible = false
              this.load()
            } else {
              this.$message.error("保存失败")
            }
          })
        }else {
          this.$message.error("表单填写有误")
        }
      })
    },
    dbCheck() {
      this.request.post("/datasource/check-conn", this.form).then(res => {
        if (res.code === '200' && res.data === true) {
          this.$message.success("测试连接成功")
        } else {
          this.$message.error("测试连接失败")
        }
      })
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/datasource/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    reset() {
      this.value = "MYSQL"
      this.dsName = ""
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
  background: #eee !important;
}
</style>
