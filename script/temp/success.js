const { GITHUB_REPOSITORY, ARTIFACT_ID, ACTION_ID } = require('./env');
const { exitWithMessage } = require('./error');

const main = async () => {
  const url = `https://github.com/${GITHUB_REPOSITORY}/actions/runs/${ACTION_ID}/artifacts/${ARTIFACT_ID}`;
  await exitWithMessage(`[点击此处获得编译产物](${url})`, false);
};

main();
