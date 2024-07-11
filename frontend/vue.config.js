const {defineConfig} = require("@vue/cli-service");

module.exports = defineConfig({
    transpileDependencies: true,

    devServer: {
        port: 3000,
        allowedHosts: [process.env.VUE_APP_API_HOST.replace(/^https?:\/\//, '')],
        host: '0.0.0.0',
        client: {
            webSocketURL: {
                hostname: process.env.VUE_APP_API_HOST.replace(/^https?:\/\//, ''),
                pathname: '/ws',
                port: 443, // Use the default HTTPS port
                protocol: 'wss', // Use secure WebSocket connection
            },
        },
/*        server: {
            type: 'https',
            options: {
            },
        },*/
    }
});