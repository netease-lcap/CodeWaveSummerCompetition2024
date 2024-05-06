const ALLOW_MODIFY_OTHERS =
  process.env.ALLOW_MODIFY_OTHERS ||
  (process.env.HEAD_REPOSITORY === process.env.BASE_REPOSITORY
    ? 'true'
    : 'false'); // 是否允许配置非依赖库文件

const LINT_WITH_PARALLEL = process.env.LINT_WITH_PARALLEL || 'true'; // lint parallel
const COMPlIE_WITH_PARALLEL = process.env.BUILD_WITH_PARALLEL || 'true'; // complie parallel
const DEPLOY_WITH_PARALLEL = process.env.UPLOAD_WITH_PARALLEL || 'true'; // deploy parallel
const AUTO_ADJUST_RESOLUTION = process.env.AUTO_ADJUST_RESOLUTION || 'true'; // fix globby error

const ONLY_ONE_PACKAGE_PER_PR = process.env.ONLY_ONE_PACKAGE_PER_PR || 'true'; // 是否阻止一个pr中修改多个package

const HEAD_BRANCH_NAME = process.env.HEAD_BRANCH_NAME || 'main';
const GITHUB_REPOSITORY =
  process.env.GITHUB_REPOSITORY || 'netease-lcap/CodeWaveAssetCompetition2024d';
const HEAD_REPOSITORY =
  process.env.HEAD_REPOSITORY || 'netease-lcap/CodeWaveAssetCompetition2024';

const BASE_REPOSITORY =
  process.env.BASE_REPOSITORY || 'netease-lcap/CodeWaveAssetCompetition2024d';
const PULL_REQUEST_ID = process.env.PULL_REQUEST_ID || '106';
const GITHUB_TOKEN = process.env.GITHUB_TOKEN;

const ACTION_ID = process.env.ACTION_ID || '8655960510';

const ARTIFACT_ID = process.env.ARTIFACT_ID || '1424334562';

const TEMP_FILE = process.env.TEMP_FILE || 'diff_stat.json';

const COMMIT_SHA = process.env.COMMIT_SHA || 'main';
const AUTHOR_NAME = process.env.AUTHOR_NAME;
const PR_EVENT_ACTION = process.env.PR_EVENT_ACTION;
const NOTIFY_CONFIG_JSON = process.env.NOTIFY_CONFIG_JSON;

const AMC_URL = process.env.AMC_URL;
const AMC_BODY = process.env.AMC_BODY;
const AMU_URL = process.env.AMU_URL;

module.exports = {
  ALLOW_MODIFY_OTHERS,
  LINT_WITH_PARALLEL,
  COMPlIE_WITH_PARALLEL,
  DEPLOY_WITH_PARALLEL,
  AUTO_ADJUST_RESOLUTION,
  GITHUB_REPOSITORY,
  HEAD_REPOSITORY,
  BASE_REPOSITORY,
  PULL_REQUEST_ID,
  GITHUB_TOKEN,
  HEAD_BRANCH_NAME,
  ONLY_ONE_PACKAGE_PER_PR,
  ACTION_ID,
  TEMP_FILE,
  ARTIFACT_ID,
  COMMIT_SHA,
  AUTHOR_NAME,
  PR_EVENT_ACTION,
  NOTIFY_CONFIG_JSON,
  AMC_URL,
  AMC_BODY,
  AMU_URL,
};
