<template>
  <div class="wrapper">
    <div style="margin: 10px 20px; font-size: 14px;">Version: {{version}}</div>
    <div style="margin: 200px auto; background-color: #fff; width: 350px; height: 380px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>登 录</b></div>
      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password
                    v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item>
            <el-checkbox v-model="user.ldapFlag" style="margin-right: 190px;">LDAP</el-checkbox>
        </el-form-item>
        <el-form-item>
            <el-button v-if="registerEnable" type="warning" size="small" style="width: 100%" autocomplete="off" @click="$router.push('/register')">注册</el-button>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" size="small" style="width: 100%" autocomplete="off" @click="login">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import {version} from "../../public/config";
export default {
  name: "Login",
  data() {
    return {
      version,
      registerEnable: true,
      user: {},
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 3, max: 10, message: '长度在 3 到 5 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
      }
    }
  },
  async mounted() {
      //  是否开启注册
      const res = await this.request.get('/user/register-enable')
      this.registerEnable = res.data
  },
  methods: {
    login() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          this.request.post("/user/login", this.user).then(res => {
            if (res.code === '200') {
              localStorage.setItem("user", JSON.stringify(res.data))  // 存储用户信息到浏览器
              // 动态设置当前用户的路由
              this.$message.success("登录成功")
              this.$router.push("/")
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      });
    }
  }
}
</script>

<style>
.wrapper {
  height: 100vh;
  background-image: linear-gradient(to bottom right, #FC466B, #3F5EFB);
  overflow: hidden;
}
</style>
