<template>
  <div>
    <div style="margin: 10px 0">
      <el-select v-model="name" placeholder="请选择所属父级菜单">
        <el-option
            v-for="(item,index) in dsNames"
            :key="index"
            :label="item"
            :value="item">
        </el-option>
      </el-select>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="isAddChildDbFlag = false;handleAdd()">新增父级菜单 <i class="el-icon-circle-plus-outline"></i></el-button>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
              row-key="id" default-expand-all>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="名称"></el-table-column>
      <el-table-column label="图标" class-name="fontSize18" align="center" label-class-name="fontSize12">
        <template slot-scope="scope">
          <span :class="scope.row.icon" />
        </template>
      </el-table-column>
      <el-table-column prop="description" type="textarea" label="描述"></el-table-column>
      <el-table-column prop="sortNum" label="顺序"></el-table-column>
      <el-table-column label="操作"  width="450" align="center">
        <template slot-scope="scope">
          <el-button type="primary" @click="isAddChildDbFlag = true;handleAdd(scope.row.id)" v-if="!scope.row.pid">新增数据库 <i class="el-icon-plus"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定同步吗？"
              @confirm="syncDs(scope.row)"
          >
            <el-button type="warning" slot="reference" v-if="!scope.row.pid">同步数据源 <i class="el-icon-refresh"></i></el-button>
          </el-popconfirm>
          <el-button type="success" class="ml-5" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
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

    <el-dialog title="数据库信息" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="80px" size="small">
        <el-form-item label="名称" v-if="this.isAddChildDbFlag">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="名称" v-else>
          <el-select clearable v-model="form.name" placeholder="请选择对应数据源" style="width: 100%">
            <el-option
                v-for="(item,index) in dsNames"
                :key="index"
                :label="item"
                :value="item">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="图标">
          <el-select clearable v-model="form.icon" placeholder="请选择" style="width: 100%">
            <el-option v-for="(item,index) in icons" :key="index" :label="item" :value="item">
              <i :class="item" /> {{ item }}
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="顺序">
          <el-input v-model="form.sortNum" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import icons from '@/assets/icon'
export default {
  name: "Database",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      form: {},
      dialogFormVisible: false,
      icons: [],
      dsNames: [],
      isAddChildDbFlag: false
    }
  },
  async created() {
    await this.loadDsNames()
    this.load()
  },
  methods: {
    async loadDsNames() {
      this.icons = icons.icons
      const res = await this.request.get("/datasource/ds-name")
      this.dsNames = res.data
      if (this.dsNames.length !== 0) {
        this.name = this.dsNames[0]
      }
    },
    load() {
      this.request.get("/database", {
        params: {
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data
      })
    },
    save() {
      this.request.post("/database", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    handleAdd(pid) {
      this.dialogFormVisible = true
      this.form = {}
      if (pid) {
        this.form.pid = pid
      }
    },
    syncDs(row) {
      this.request.get("/datasource/sync",{
        params: {
          name: row.name,
          pid: row.id,
        }
      }).then(res => {
        if (res.code === '200') {
          this.$message.success("同步完成")
          this.load()
        } else {
          this.$message.error("同步失败")
        }
      })
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      if (this.form.pid){
        this.isAddChildDbFlag = true;
      }else {
        this.isAddChildDbFlag = false;
      }
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/database/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
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
.fontSize18{
  font-size: 18px;
}
.fontSize12{
  font-size: 12px;
}
</style>
