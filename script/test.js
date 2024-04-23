const { execCommand } = require('./utils/execCommand');

const tryAmendCommit = async (forceNew = false) => {
  const ans = await execCommand('git rev-list --count main..HEAD');
  const count = parseInt(ans.trim(), 10);
  if (!forceNew && count > 0) {
    await execCommand('git add . && git commit -q --amend --no-edit');
  } else {
    const ans = await execCommand(
      `git add . && git commit -q -m "doc: generate changelog by ci"`,
    );
    console.log('githubbot commit:\n', ans);
    const hhh = '2';
  }
};

tryAmendCommit(true).catch((e) => console.error(e));
