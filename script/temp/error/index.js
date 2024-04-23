const path = require("path");
const fsp = require("fs/promises");

const {
  GITHUB_REPOSITORY,
  HEAD_BRANCH_NAME,
  ACTION_ID,
  HEAD_REPOSITORY,
  PULL_REQUEST_ID,
} = require("../env");

const footer = `
资产共建大赛提交内容中原有的截图，现升级为依赖库使用说明文档。现交内容包括

依赖库代码本身；2. readme代码说明文档；3. 依赖库使用文档说明。
其中readme面向开发者，使用文档面向使用者。
请自己阅读提交作品路径说明。
https://github.com/netease-lcap/CodeWaveAssetCompetition2024/blob/main/%E8%B5%84%E4%BA%A7%E5%85%B1%E5%BB%BA%E5%A4%A7%E8%B5%9B%E6%8F%90%E4%BA%A4%E5%86%85%E5%AE%B9%E8%AF%B4%E6%98%8E.md

参考作品https://github.com/netease-lcap/CodeWaveAssetCompetition2024/tree/main/demo_bjz_JiazhenBao

tips：README.md和依赖库使用文档说明.docx需要保持文件名称不动。后续我们会对提交的PR自动校验目录结构~
`;

const exitWithMessage = async (message, hasError = true) => {
  const url = `https://github.com/${GITHUB_REPOSITORY}/actions/runs/${ACTION_ID}`;
  try {
    const m = `关联[ACTION](${url})\n\n${message}`;
    await fsp.writeFile("test.md", m, "utf-8");
    await fsp.writeFile(
      "pr_result.json",
      JSON.stringify({
        message: m,
        hasError,
        pull_request_id: PULL_REQUEST_ID,
      }),
      "utf-8"
    );
  } catch (e) {
    console.error(`IMPORTANT: exitWithMessage ~ m[exitCode]:`, e);
  } finally {
    process.exit(0); // normal exit
  }
};

const erroredPackagesToMsg = (packages) => {
  const str = packages.map(toError).join("\n\n");
  return str;
  function toError({ error, packageName, packageRoot }) {
    const _errorMsg = error.message || `${error}`;
    const errorMsg = _errorMsg.split("\n").slice(-100).join("\n");
    const packgeRootURL = `https://github.com/${HEAD_REPOSITORY}/tree/${HEAD_BRANCH_NAME}/${packageRoot}`;
    const msg = `[${
      packageName || packageRoot
    }](${packgeRootURL})存在错误：\n\`\`\`bash\n${errorMsg}\n\`\`\``;
    return msg;
  }
};

const prettyFileTree = (filenames) => {
  const treeObj = {};
  filenames.forEach((filename) => {
    const tmp = filename.split(path.sep);
    tmp.reduce((acc, value, idx) => {
      const isLast = idx === tmp.length - 1;
      if (isLast) {
        acc[value] = true;
        return acc;
      } else {
        acc[value] = acc[value] || {};
        return acc[value];
      }
    }, treeObj);
  });
  return generateFileTree(treeObj);

  function generateFileTree(obj, prefix = "") {
    let tree = "";
    const files = Object.entries(obj);

    files.forEach(([key, value], index) => {
      const isLast = index === files.length - 1;

      tree += `${prefix}${isLast ? "└──" : "├──"} ${key}\n`;

      if (typeof value === "object") {
        const newPrefix = `${prefix}${isLast ? "   " : "│  "}`;

        tree += generateFileTree(value, newPrefix);
      }
    });

    return tree;
  }
};

const unsupportedFileToMsg = (filenames) => {
  const str = `意外修改了以下文件，这些文件目前无法被当前用户修改:\n\`\`\`bash\n${prettyFileTree(
    filenames
  )}\n\`\`\``;
  return str + footer;
};

const toManyPackagesToMsg = (packages) => {
  const str = packages.map(toError).join("\n\n");
  return `存在多处package修改:\n\n${str}\n${footer}`;

  function toError({ packageName, cwd, changedFiles }) {
    const packgeRootURL = `https://github.com/${HEAD_REPOSITORY}/tree/${HEAD_BRANCH_NAME}/${cwd}`;
    const msg = `[${packageName}](${packgeRootURL})涉及的修改：\n\`\`\`bash\n${prettyFileTree(
      changedFiles
    )}\n\`\`\``;
    return msg;
  }
};

module.exports = {
  exitWithMessage,
  erroredPackagesToMsg,
  unsupportedFileToMsg,
  toManyPackagesToMsg,
};
