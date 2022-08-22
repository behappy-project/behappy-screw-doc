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
      <el-select v-model="value" placeholder="请选择数据源" class="ml-5">
        <el-option
            v-for="(item,index) in options"
            :key="index"
            :label="item"
            :value="item">
        </el-option>
      </el-select>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0" v-if="user.role === 'ROLE_ADMIN'">
      <el-tooltip placement="top">
        <div slot="content">新文档生成适用<br/>生成文档不会要求输入更新内容</div>
        <el-popconfirm
            confirm-button-text='确定'
            cancel-button-text='我再想想'
            icon="el-icon-info"
            icon-color="red"
            title="您确定生成这些数据库的文档吗？"
            @confirm="createBatch"
        >
          <el-button type="danger" slot="reference">生成文档 <i class="el-icon-plus"></i></el-button>
        </el-popconfirm>
      </el-tooltip>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="icon" label="图标" width="125" align="center">
        <template slot-scope="scope">
          <i :class="scope.row.icon"></i>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="数据库名称" align="center">
        <template slot-scope="scope">
          <a target="_blank" :href="serverIp+'/doc/'+scope.row.dsName+'/'+scope.row.name+'.html?token='+user.token"
             style="white-space:nowrap"><u>{{ scope.row.name }}</u></a>
        </template>
      </el-table-column>
      <el-table-column prop="description" type="textarea" label="描述" align="center"></el-table-column>
      <el-table-column label="操作" width="280" align="center">
        <template slot-scope="scope">
          <el-tooltip placement="top">
            <div slot="content">更新文档生成适用<br/>更新文档要求输入本次更新内容,以便之后进行历史回溯</div>
            <el-popconfirm
                class="ml-5"
                confirm-button-text='确定'
                cancel-button-text='我再想想'
                icon="el-icon-info"
                icon-color="red"
                title="您确定更新吗？"
                @confirm="dialogFormVisible = true;updateDocId = scope.row.id"
            >
              <el-button type="danger" slot="reference">更新文档 <i class="el-icon-plus"></i></el-button>
            </el-popconfirm>
          </el-tooltip>
          <el-button style="margin-left: 10px" type="info" slot="reference"
                     @click="dataBaseHistoryId = scope.row.id;findHistory()">查看历史回溯
          </el-button>
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

    <el-dialog title="本次更新信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="90px" size="small">
        <el-form-item label="请输入内容">
          <el-input type="textarea"
                    :autosize="{ minRows: 2, maxRows: 4}"
                    placeholder="限制1000字符"
                    v-model="updateDocContent" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateDoc">更 新</el-button>
      </div>
    </el-dialog>

    <!--查看历史回溯-->
    <el-dialog title="历史回溯" :visible.sync="historyFormVisible" width="35%">
      <div class="infinite-list"
           v-infinite-scroll="dialogFindHistory"
           :infinite-scroll-immediate="false"
           style="height:500px;overflow-y:auto">
        <el-timeline v-for="item in databaseHistory" :key="item.historyId" class="infinite-list-item">
          <el-timeline-item :timestamp="formatFormDate(new Date(item.updateTime))" placement="top">
            <el-card>
              <h4>{{ item.description }}</h4>
              <p>{{ item.updateUser }} 更新于 {{ formatFormDateTime(new Date(item.updateTime)) }}</p>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="historyFormVisible = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {serverIp} from "../../public/config";
import {parseTime} from "@/utils/commonUtil";

export default {
  name: "Doc",
  data() {
    return {
      serverIp,
      tableData: [],
      databaseHistory: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      multipleSelection: [],
      user: JSON.parse(localStorage.getItem("user")),
      options: [],
      value: 'MYSQL',
      dialogFormVisible: false,
      // 查看历史回溯
      historyFormVisible: false,
      updateDocId: '',
      updateDocContent: '',
      dsNames: [],
      // 历史回溯,数据库id
      dataBaseHistoryId: '',
      // 已查看历史回溯总数
      haveWatchedDataBaseHistoryCount: 0
    }
  },
  async created() {
    await this.getDatasourceType()
    this.load()
  },
  methods: {
    formatFormDate(date) {
      return parseTime(date, '{y}-{m}-{d}')
    },
    formatFormDateTime(date) {
      return parseTime(date)
    },
    async getDatasourceType() {
      //  获取数据源类型
      const optionRes = await this.request.get('/database/options')
      this.options = optionRes.data
      const dsNameRes = await this.request.get("/datasource/ds-name")
      this.dsNames = dsNameRes.data
      if (this.dsNames.length !== 0) {
        this.name = this.dsNames[0]
      }
    },
    load() {
      this.request.post("/database/schemas", {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        name: this.name,
        dataSource: this.value,
        role: this.user.role
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    async findHistory() {
      this.haveWatchedDataBaseHistoryCount = 5
      const res = await this.request.get(`/database/history/${this.dataBaseHistoryId}/${this.haveWatchedDataBaseHistoryCount}`,)
      this.databaseHistory = res.data.records
      this.historyFormVisible = true
    },
    async dialogFindHistory() {
      this.haveWatchedDataBaseHistoryCount += 5
      const res = await this.request.get(`/database/history/${this.dataBaseHistoryId}/${this.haveWatchedDataBaseHistoryCount}`,)
      this.databaseHistory = res.data.records
    },
    updateDoc() {
      this.request.post("/screw/update-doc", {
        id: this.updateDocId,
        dataSourceEnum: this.value,
        updateDocContent: this.updateDocContent
      }).then(res => {
        if (res.code === '200') {
          this.$message.success("更新文档成功")
          this.load()
        } else {
          this.$message.error("更新文档失败")
        }
        this.dialogFormVisible = false
        this.updateDocContent = ''
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
      if (this.dsNames.length !== 0) {
        this.name = this.dsNames[0]
      }
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


<style scoped>
.headerBg {
  background: #eee !important;
}

.sketch_content {
  overflow: auto;
  height: 434px;
  border-top: 1px solid #eff1f4;
  border-bottom: 1px solid #eff1f4;
  padding: 0px 30px 11px 27px;
}

.sketch_content::-webkit-scrollbar {
  width: 3px;
}

.sketch_content::-webkit-scrollbar-thumb {
  background: #8798AF;
  border-radius: 2px;
}

.sketch_content::-webkit-scrollbar-track {
  background: transparent;
}
</style>
