import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'Manage',
        component: () => import('../views/Manage.vue'),
        redirect: "/home",
        children: [
            {
                name: "主页",
                path: "/home",
                component: () => import('../views/Home.vue')
            },
            {
                path: '/person',
                name: '个人信息',
                component: () => import('../views/Person.vue')
            },
            {
                path: '/password',
                name: '修改密码',
                component: () => import('../views/Password.vue')
            },
            {
                name: "用户管理",
                path: "/user",
                component: () => import('../views/User.vue')
            },
            {
                name: "角色管理",
                path: "/role",
                component: () => import('../views/Role.vue')
            },
            {
                name: "数据源配置",
                path: "/datasource",
                component: () => import('../views/Datasource.vue')
            },
            {
                name: "数据库管理",
                path: "/database",
                component: () => import('../views/Database.vue')
            },
            {
                name: "文档管理",
                path: "/doc",
                component: () => import('../views/Doc.vue')
            }
        ]
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/Login.vue')
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import('../views/Register.vue')
    },
    {
        path: '/404',
        name: '404',
        component: () => import('../views/404.vue')
    }
]

const router = new VueRouter({
    mode: 'hash',
    base: process.env.BASE_URL,
    routes
})

router.beforeEach((to, from, next) => {
    localStorage.setItem("currentPathName", to.name)  // 设置当前的路由名称
    store.commit("setPath")
    // 注册和登陆页面跳过
    let paths = ['/login', '/register']
    // 判断是否登陆
    const token = localStorage.getItem("user")
    if (!token) {
        console.log(to.matched)
        if (to.matched.some(metadata => paths.includes(metadata.path))) {
            next()
        }
        // 未找到路由的情况
        else if (!to.matched.length) {
            next("/404")
        } else {
            next("/login")
        }
    }
    // 其他的情况都放行
    next()
})

export default router
