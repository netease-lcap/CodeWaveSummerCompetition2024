const { execCommand } = require('./execCommand');

const branchReg = /^task(?:\(([a-zA-Z\-\_\d]+)\))?\-([a-zA-Z\-\_\d]+)\-/i;

module.exports.getBranchInfo = async () => {
  const branchName = await execCommand('git branch --show-current');
  const matchResult = branchReg.exec(branchName);
  console.log('branch name:', branchName);
  const libraryName = matchResult[1];
  const taskId = matchResult[2];
  return [libraryName, taskId];
};
