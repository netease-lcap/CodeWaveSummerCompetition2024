const fsp = require('fs/promises');
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
  await execCommand('npm run usage', {
    throwWhenStderr: false,
    cwd,
  });
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
