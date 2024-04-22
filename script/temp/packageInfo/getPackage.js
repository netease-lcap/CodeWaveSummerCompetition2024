const fs = require('fs');
const path = require('path');
const semver = require('semver');
const { XMLParser } = require('fast-xml-parser');

const { execCommand } = require('../utils/execCommand');

const fsp = fs.promises;

const getTagVersion = async (packageName) => {
  try {
    const tagRaw = await execCommand(
      `git describe --tags --abbrev=0 --match '${packageName}*'`,
    );
    const [tag] = tagRaw.split(/\s/);
    const result = new RegExp(`${packageName}@(\S+)`).exec(tag);
    if (result) {
      return result[1];
    }
    return null;
  } catch {
    return null;
  }
};

const getNpmVersion = async (packageName) => {
  try {
    const str = await execCommand(`npm view ${packageName} version`);
    return str.trim();
  } catch {
    return null;
  }
};

const getNextVersion = async (packageName, currentVersion) => {
  let targetVersion = currentVersion || '0.0.1';
  const tagVersion = await getTagVersion(packageName);
  if (tagVersion && semver.gt(tagVersion, targetVersion)) {
    targetVersion = tagVersion;
  }
  const npmVersion = await getNpmVersion(packageName);
  if (npmVersion && semver.gt(npmVersion, targetVersion)) {
    targetVersion = npmVersion;
  }
  return targetVersion === currentVersion
    ? currentVersion
    : semver.inc(targetVersion, 'patch');
};

const readPackageJSON = async (filePath) => {
  try {
    const fileContent = await fsp.readFile(filePath, 'utf-8');
    const packageInfo = JSON.parse(fileContent);
    if (!packageInfo.name || !packageInfo.version) throw new Error();
    return packageInfo;
  } catch {
    throw new Error('不存在合法的package.json文件');
  }
};

const getBackendPackage = async (packageRoot) => {
  try {
    const filePath = path.resolve(packageRoot, 'pom.xml');
    const fileContent = await fsp.readFile(filePath, 'utf-8');
    const parser = new XMLParser();
    const packageInfo = parser.parse(fileContent);
    const packageName = packageInfo.project.artifactId;
    const nextVersion = packageInfo.project.version;
    return {
      cwd: path.resolve(packageRoot),
      packageInfo,
      packageRoot,
      packageName,
      nextVersion,
      type: 'b',
    };
  } catch {
    return null;
  }
};

const getFrontendPackage = async (packageRoot) => {
  try {
    const filePath = path.resolve(packageRoot, 'package.json');
    const packageInfo = await readPackageJSON(filePath);
    const packageName = packageInfo.name;
    const nextVersion = await getNextVersion(packageName, packageInfo.version);
    return {
      cwd: path.resolve(packageRoot),
      packageInfo,
      packageRoot,
      packageName,
      nextVersion,
      type: 'f',
    };
  } catch {
    return null;
  }
};

const getPackage = async (packageRoot) => {
  const be = await getBackendPackage(packageRoot);
  if (be) return be;
  const fe = await getFrontendPackage(packageRoot);
  if (fe) return fe;
  throw new Error('不存在合法的package配置');
};

module.exports = { getPackage };

// main('packages/libraryC').catch((e) => {
//   console.error(e);
// });
