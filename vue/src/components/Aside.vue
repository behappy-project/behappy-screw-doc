<template>
  <el-menu style="min-height: 100%; overflow-x: hidden"
           background-color="rgb(48, 65, 86)"
           text-color="#fff"
           active-text-color="#ffd04b"
           :collapse-transition="false"
           :collapse="isCollapse"
           router
  >
    <div style="height: 60px; line-height: 60px; text-align: center">
      <img src="../assets/screw_doc.png" alt="" style="width: 20px; position: relative; top: 5px;">
      <b style="color: white; margin-left: 5px" v-show="logoTextShow">数据库文档管理</b>
    </div>
    <el-menu-item index="/home">
      <i class="el-icon-s-home"></i>
      <span slot="title">主页</span>
    </el-menu-item>
    <el-submenu v-if="this.user.role === 'ROLE_ADMIN'" index="">
      <template slot="title">
        <i class="el-icon-menu"></i>
        <span slot="title">系统管理</span>
      </template>
      <el-menu-item index="/user">
        <template slot="title">
          <i class="el-icon-user"></i>
          <span slot="title">用户管理</span>
        </template>
      </el-menu-item>
      <el-menu-item index="/role">
        <template slot="title">
          <i class="el-icon-s-custom"></i>
          <span slot="title">角色管理</span>
        </template>
      </el-menu-item>
      <el-menu-item index="/database">
        <template slot="title">
          <i class="el-icon-s-flag"></i>
          <span slot="title">数据库管理</span>
        </template>
      </el-menu-item>
    </el-submenu>
    <el-menu-item index="/doc">
      <i class="el-icon-document"></i>
      <span slot="title">文档管理</span>
    </el-menu-item>
  </el-menu>
</template>

<script>

export default {
  name: "Aside",
  props: {
    isCollapse: Boolean,
    logoTextShow: Boolean
  },
  data() {
    return {
      user: JSON.parse(localStorage.getItem("user"))
    }
  }
}
</script>

<style>
.el-menu-item.is-active {
  background-color: rgb(38, 52, 69) !important;
}

.el-menu-item:hover {
  background-color: rgb(38, 52, 69) !important;
}

.el-submenu__title:hover {
  background-color: rgb(38, 52, 69) !important;
}

/*解决收缩菜单文字不消失问题*/
.el-menu--collapse span {
  visibility: hidden;
}
</style>
