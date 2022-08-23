const plugins = []
// 生产环境移除console
if (process.env.NODE_ENV === 'production') {
    plugins.push("transform-remove-console")
}
module.exports = {
    presets: ["@vue/cli-plugin-babel/preset"],
    plugins: plugins
}