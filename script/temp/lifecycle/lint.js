const { glob } = require('glob');

const FE_PREFERED_FILE = {
  'package.json': '缺少package.json',
  'components/*/*.js': '缺少component代码',
  'components/*/*.yaml': '缺少component声明',
  'logics/*/*.js': '缺少logic代码',
};

const BE_PREFERED_FILE = {
  'src/**/*.java': '缺少java代码',
  'pom.xml': '缺少pom.xml',
  'README.md': '缺少README.md',
  '依赖库使用文档说明.docx': '缺少依赖库使用文档说明.docx',
};

const createGlobDetecter = (info) => {
  const rules = Object.entries(info);
  return async (package) => {
    const { cwd } = package;
    const _errors = await Promise.all(
      rules.map(async ([search, message]) => {
        const files = await glob(search, { cwd });
        if (files.length === 0) return message;
        return null;
      }),
    );

    let error = _errors.filter(Boolean);
    if (package.type === 'f') {
      const tmp = error.reduce((acc, v) => {
        acc[v] = true;
        return acc;
      }, {});
      if (!tmp['缺少component代码']) {
        // 存在组件代码
        delete tmp['缺少logic代码'];
      } else if (!tmp['缺少logic代码']) {
        // 存在逻辑代码
        delete tmp['缺少component代码'];
        delete tmp['缺少component声明'];
      }
      error = Object.keys(tmp);
    }
    if (error.length > 0) {
      throw new Error(
        `当前package缺乏必要文件：\n${error.map((x) => ` x ${x}`).join('\n')}`,
      );
    }
  };
};

const feGlobDetecter = createGlobDetecter(FE_PREFERED_FILE);
const beGlobDetecter = createGlobDetecter(BE_PREFERED_FILE);

const feLint = async (package) => {
  return feGlobDetecter(package);
};
const beLint = async (package) => {
  return beGlobDetecter(package);
};

const lint = async (package) => {
  if (package.type === 'f') return feLint(package);
  return beLint(package);
};

module.exports = {
  lint,
};

// lint({
//   cwd: '/Users/hanshijie/myproject/git_try/packages/libraryB',
//   type: 'f',
// });
