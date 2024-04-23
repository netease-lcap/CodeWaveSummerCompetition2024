const { AUTHOR_NAME } = require("./env");

const customCheck = async ({
  processPackageWithParallelFlag,
  processPackagesErrors,
  getErroredPackages,
  getValidPackages,
}) => {
  await processPackageWithParallelFlag(
    async ({ packageRoot }) => {
      const [pathname] = packageRoot.split("/");
      if (pathname && AUTHOR_NAME) {
        if (
          pathname.endsWith(AUTHOR_NAME) ||
          pathname.startsWith(AUTHOR_NAME)
        ) {
          return;
        }
        throw new Error(
          `[${AUTHOR_NAME}]修改了他人提交的package[${pathname}]内部的文件`
        );
      }
    },
    getValidPackages(),
    true
  );
  await processPackagesErrors(getErroredPackages());
};

module.exports = {
  customCheck,
};
