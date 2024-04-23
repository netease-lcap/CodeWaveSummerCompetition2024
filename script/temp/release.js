const fsp = require('fs/promises');
const {
  COMPlIE_WITH_PARALLEL,
  DEPLOY_WITH_PARALLEL,
  TEMP_FILE,
} = require('./env');
const crypto = require('crypto');
const { complie, deploy } = require('./lifecycle');

const detect = require('./detect');
const { execCommands, execCommand } = require('./utils/execCommand');
const path = require('path');
const { glob } = require('glob');

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
    async ({ cwd, packageInfo, nextVersion, type }) => {
      if (type !== 'f') return;
      const [zipFile] = await glob(['target/*.zip', '*.zip'], { cwd });
      let md5 = '';
      if (zipFile) {
        const fileContent = await fsp.readFile(path.resolve(cwd, zipFile));
        const hash = crypto.createHash('md5');
        hash.update(fileContent);
        md5 = hash.digest('hex');
      }
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
      );
      await execCommand(`git tag ${packageInfo.name}@${nextVersion}`);
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
  // deploy
  await processPackageWithParallelFlag(
    deploy,
    getValidPackages(),
    DEPLOY_WITH_PARALLEL === 'true',
  );
  await processPackagesErrors(getErroredPackages());
};

main();
