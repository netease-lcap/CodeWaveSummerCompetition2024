const { execCommand } = require('./utils/execCommand');

/**
 * 当前commit相较于origin/main的变更内容
 */
const getChangedFileFromRange = async () => {
  const fileStr = await execCommand('git diff --name-only origin/main...HEAD');
  return fileStr
    .split('\n')
    .map((v) => v.trim())
    .filter(Boolean);
};

/**
 *  当前commit存在的变更内容
 */
const getChangedFile = async () => {
  const fileStr = await execCommand(
    'git show -m --name-only --pretty=format: --first-parent HEAD',
  );
  return fileStr
    .split('\n')
    .map((v) => v.trim())
    .filter(Boolean);
};

module.exports = {
  getChangedFileFromRange,
  getChangedFile,
};
