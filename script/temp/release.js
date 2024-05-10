const fsp = require('fs/promises');
const {
  COMPlIE_WITH_PARALLEL,
  DEPLOY_WITH_PARALLEL,
  TEMP_FILE,
  AMC_URL,
  AMU_URL,
  AMC_BODY,
} = require('./env');
const crypto = require('crypto');
const { complie, deploy } = require('./lifecycle');

const detect = require('./detect');
const { execCommands, execCommand } = require('./utils/execCommand');
const path = require('path');
const { glob } = require('glob');
const fetch = require('node-fetch');

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
  if (erroredPackages.length > 0) {
    process.exit(1);
  }
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
  const { packages } = await tryGetPackagesFromTempFile();

  const getValidPackages = () => packages.filter((p) => !p.error);
  const getErroredPackages = () => packages.filter((p) => !!p.error);

  await processPackagesErrors(getErroredPackages());

  // comlie
  await processPackageWithParallelFlag(
    complie,
    getValidPackages(),
    COMPlIE_WITH_PARALLEL === 'true',
  );
  await processPackagesErrors(getErroredPackages());

  await processPackageWithParallelFlag(
    async ({ cwd, packageInfo, nextVersion, packageName, type }) => {
      const [zipFile] = await glob(['target/*.zip', '*.zip'], { cwd });
      let md5 = '';
      if (zipFile) {
        const fileContent = await fsp.readFile(path.resolve(cwd, zipFile));
        const hash = crypto.createHash('md5');
        hash.update(fileContent);
        md5 = hash.digest('hex');
      }
      if (type === 'f') {
        await fsp.writeFile(
          path.resolve(cwd, 'package.json'),
          JSON.stringify(
            Object.assign(
              {
                ...packageInfo,
                version: nextVersion,
              },
              md5 && { lastRelease: md5 },
            ),
            null,
            2,
          ),
          'utf-8',
        );
      } else {
        await fsp.writeFile(path.resolve(cwd, '.lastRelease'), md5, 'utf-8');
      }
      await execCommand(`git tag ${packageName}@${nextVersion}`);
    },
    getValidPackages(),
    true,
  );
  await processPackagesErrors(getErroredPackages());
  if (getValidPackages().length > 0) {
    await execCommands([
      'git add . ',
      'git commit -q -m "publish version by ci"',
      'git pull && git push --force-with-lease',
    ]);
  }
  await fsp.mkdir('dist');
  let uploadFn = () => Promise.resolve(null);
  if (AMC_URL && AMU_URL && AMC_BODY) {
    // todo: 上传到资产市场，当前为测试参数，请调整为正常接口参数。
    const { token } = await fetch(AMC_URL, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Accept: 'application/json',
      },
      body: AMC_BODY,
    }).then((v) => v.json());

    uploadFn = async (form) => {
      await fetch(AMU_URL, {
        method: 'POST',
        body: form,
        headers: {
          Authorization: `Bearer ${token}`,
          ...form.getHeaders(),
        },
      });
      return 'ok';
    };
  }
  // deploy
  await processPackageWithParallelFlag(
    (info) => deploy(info, uploadFn),
    getValidPackages(),
    DEPLOY_WITH_PARALLEL === 'true',
  );
  await processPackagesErrors(getErroredPackages());
};

main();
