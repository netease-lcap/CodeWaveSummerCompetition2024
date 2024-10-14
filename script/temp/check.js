const fsp = require('fs/promises');
const {
  ALLOW_MODIFY_OTHERS,
  LINT_WITH_PARALLEL,
  COMPlIE_WITH_PARALLEL,
  DEPLOY_WITH_PARALLEL,
  ONLY_ONE_PACKAGE_PER_PR,
  TEMP_FILE,
  PR_EVENT_ACTION,
  NOTIFY_CONFIG_JSON,
  PULL_REQUEST_ID,
  BASE_REPOSITORY,
} = require('./env');
const { lint, complie, deploy } = require('./lifecycle');
const {
  exitWithMessage,
  erroredPackagesToMsg,
  unsupportedFileToMsg,
  toManyPackagesToMsg,
} = require('./error');
const { customCheck } = require('./customCheck');
const nodemailer = require('nodemailer');

const detect = require('./detect');

const processPackageWithParallelFlag = (fn, packages, isParallel) => {
  if (isParallel) {
    return Promise.all(packages.map((p) => processPackage(p)));
  } else {
    return packages.reduce(async (acc, p) => {
      const prevPackage = await acc;
      if (prevPackage.error) return prevPackage;
      return processPackage(p);
    }, Promise.resolve({ error: null }));
  }

  async function processPackage(package) {
    try {
      if (package.error) throw package.error;
      await fn(package);
      return package;
    } catch (e) {
      package.error = e;
      return package;
    }
  }
};

const processPackagesErrors = async (erroredPackages) => {
  if (erroredPackages.length > 0)
    await exitWithMessage(erroredPackagesToMsg(erroredPackages));
};

const tryGetPackagesFromTempFile = async () => {
  const tmpResult = await getFromTempFile();
  if (tmpResult) {
    return tmpResult;
  }
  const info = await detect();
  return info;

  async function getFromTempFile() {
    try {
      const fileContent = await fsp.readFile(TEMP_FILE, 'utf-8');
      return JSON.parse(fileContent);
    } catch {
      return null;
    }
  }
};

const main = async () => {
  if (PR_EVENT_ACTION === 'opened' && NOTIFY_CONFIG_JSON) {
    try {
      const { mailOption, subscribers } = JSON.parse(NOTIFY_CONFIG_JSON);
      const transporter = nodemailer.createTransport(mailOption);
      const prURI = `https://github.com/${BASE_REPOSITORY}/pull/${PULL_REQUEST_ID}`;
      await transporter.sendMail({
        from: `"Github Action 👻" <${mailOption.auth.user}>`, // sender address
        to: subscribers.join(', '), // list of receivers
        subject: `New Pull Request In ${BASE_REPOSITORY}`, // Subject line
        text: `Hello, new pull request opened in ${BASE_REPOSITORY}. [**Click here to see the content**](${prURI})`, // plain text body
        html: `<span>Hello, new pull request opened in ${BASE_REPOSITORY}. <b><a href="${prURI}">Click here to see the content!</a></b></span>`, // html body
      });
    } catch (e) {
      console.log('There is error in send email');
      console.error(e);
    }
  }
  const { packages, noPackageFiles } = await tryGetPackagesFromTempFile();

  const getValidPackages = () => packages.filter((p) => !p.error);
  const getErroredPackages = () => packages.filter((p) => !!p.error);

  console.log(
    '🚀 ~ file: check.js:75 ~ main ~ ALLOW_MODIFY_OTHERS:',
    ALLOW_MODIFY_OTHERS,
  );
  if (noPackageFiles.length > 0) {
    if (ALLOW_MODIFY_OTHERS === 'true') {
      console.warn(
        '本分支修改了非依赖库文件，ls:\n',
        noPackageFiles.join('\n'),
      );
    } else {
      return exitWithMessage(unsupportedFileToMsg(noPackageFiles));
    }
  }

  await processPackagesErrors(getErroredPackages());
  const validPackages = getValidPackages();

  if (ONLY_ONE_PACKAGE_PER_PR === 'true' && validPackages.length > 1) {
    await exitWithMessage(toManyPackagesToMsg(validPackages));
  }

  // custom check for temp
  await customCheck({
    packages,
    exitWithMessage,
    processPackageWithParallelFlag,
    getValidPackages,
    getErroredPackages,
    processPackagesErrors,
  });

  // lint
  // await processPackageWithParallelFlag(
  //   lint,
  //   getValidPackages(),
  //   LINT_WITH_PARALLEL === 'true',
  // );
  // await processPackagesErrors(getErroredPackages());

  // comlie
  await processPackageWithParallelFlag(
    complie,
    getValidPackages(),
    COMPlIE_WITH_PARALLEL === 'true',
  );
  await processPackagesErrors(getErroredPackages());

  await fsp.mkdir('dist');
  // deploy
  await processPackageWithParallelFlag(
    deploy,
    getValidPackages(),
    DEPLOY_WITH_PARALLEL === 'true',
  );
  await processPackagesErrors(getErroredPackages());

  await exitWithMessage('successful', false);
};

main();
