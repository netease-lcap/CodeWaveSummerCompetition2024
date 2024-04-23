const path = require('path');
const { glob } = require('glob');
const { execCommand } = require('../utils/execCommand');
const { createTagAndRelease, uploadReleaseAssets } = require('../github');

const isRelease = process.argv[2] === 'from-release';
const deploy = async ({ cwd, packageName, nextVersion, type }) => {
  const [zipFile] = await glob(['target/*.zip', '*.zip'], { cwd });
  const [docxFile] = await glob('依赖库使用文档说明.docx', { cwd });
  if (isRelease) {
    if (type !== 'f') return;
    const id = await createTagAndRelease(`${packageName}@${nextVersion}`);
    await uploadReleaseAssets(
      id,
      [
        zipFile && {
          filename: path.resolve(cwd, zipFile),
          name: path.basename(zipFile),
        },
        docxFile && {
          filename: path.resolve(cwd, docxFile),
          name: `${packageName}.docx`,
        },
      ].filter(Boolean),
    );
  } else {
    if (zipFile) {
      await execCommand(
        `cp ${path.resolve(cwd, zipFile)} dist/${zipFile.replace(
          /^target\//,
          '',
        )}`,
      );
    } else {
      throw new Error('不存在指定的zip');
    }
    if (docxFile)
      await execCommand(
        `cp ${path.resolve(cwd, docxFile)} dist/${packageName}_${docxFile}`,
      );
  }
};

module.exports = {
  deploy,
};
