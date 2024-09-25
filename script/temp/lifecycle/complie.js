const fsp = require('fs/promises');
const fs = require('fs');
const path = require('path');
const { execCommands, execCommand } = require('../utils/execCommand');
const { AUTO_ADJUST_RESOLUTION } = require('../env');

const feBuild = async (package) => {
  const { cwd, nextVersion, packageInfo } = package;
  packageInfo.version = nextVersion;
  if (
    packageInfo &&
    !packageInfo.resolutions &&
    AUTO_ADJUST_RESOLUTION === 'true'
  ) {
    packageInfo.resolutions = {
      globby: '9.2.0',
    };
  }

  await fsp.writeFile(
    path.resolve(cwd, 'package.json'),
    JSON.stringify(packageInfo, null, 2),
    'utf-8',
  );

  await execCommands(['npm install --force', 'npm run build'], {
    cwd,
  });

  // 检查 tsconfig.json 是否存在
  const tsconfigPath = path.resolve(cwd, 'tsconfig.json');
  try {
    await fsp.access(tsconfigPath, fs.constants.F_OK);
    console.log('tsconfig.json 存在，不执行 "npm run usage" 命令。');
  } catch (err) {
    console.log(tsconfigPath);
    console.log(err);
    // 如果 tsconfig.json 不存在，则执行 "npm run usage"
    await execCommand('npm run usage', {
      throwWhenStderr: false,
      cwd,
    });
  }
};

const beBuild = async ({ cwd }) => {
  await execCommand('mvn clean package -DskipTests', {
    cwd,
  });
};

const complie = async (package) => {
  if (package.type === 'f') return feBuild(package);
  return beBuild(package);
};

module.exports = {
  complie,
};
