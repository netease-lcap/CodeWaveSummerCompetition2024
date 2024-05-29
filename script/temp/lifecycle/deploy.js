const path = require('path');
const { glob } = require('glob');
const { execCommand } = require('../utils/execCommand');
const { createTagAndRelease, uploadReleaseAssets } = require('../github');
const FormData = require('form-data');
const fs = require('fs');

const isRelease = process.argv[2] === 'from-release';
const deploy = async ({ cwd, packageName, nextVersion }, uploadFn) => {
  const [zipFile] = await glob(['target/*.zip', '*.zip'], { cwd });
  const [docxFile] = await glob('*.docx', { cwd });
  if (isRelease) {
    const id = await createTagAndRelease(`${packageName}@${nextVersion}`);
    if (uploadFn) {
      // todo: 上传到资产市场，当前为测试参数，请调整为正常接口参数。
      const form = new FormData();
      form.append(
        path.basename(zipFile),
        fs.createReadStream(path.resolve(cwd, zipFile)),
      );
      form.append(
        `${packageName}.docx`,
        fs.createReadStream(path.resolve(cwd, docxFile)),
      );
      await uploadFn(form);
    }
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
