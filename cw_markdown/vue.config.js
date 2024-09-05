module.exports = {
  css: {
    loaderOptions: {
      less: {
        lessOptions: {
          javascriptEnabled: true,
        },
      },
    },
  },
  chainWebpack: (config) => {
    config.module
      .rule("svg")
      .use("file-loader")
      .loader(require.resolve("file-loader"))
      .tap((options) =>
        Object.assign(options, {
          limit: 20000,
        })
      );
  },
};
