const fsp = require("fs/promises");
const path = require("path");
const { TEMP_FILE } = require("./env");
const { getPackage } = require("./packageInfo/getPackage");
const { getPackageRoot } = require("./packageInfo/getPackageRoot");
const { getChangedFileFromRange, getChangedFile } = require("./git");

const createLibraryStore = () => {
  const store = new Map();
  const noPackageFiles = [];

  return {
    process,
    getAllNoPackageFile,
    getAllPackages,
  };

  async function process(pathname) {
    const packageRoot = getPackageRoot(pathname);
    if (!packageRoot) {
      noPackageFiles.push(pathname);
      return;
    }
    const existPackage = store.get(packageRoot);
    if (existPackage) {
      existPackage.changedFiles = existPackage.changedFiles || [];
      existPackage.changedFiles.push(pathname);
      return existPackage;
    }
    const package = {};
    store.set(packageRoot, package);
    const info = await getPackage(packageRoot).catch((e) => {
      return {
        packageRoot,
        error: {
          message: e.message || `${e.message}`,
        },
      };
    });
    Object.assign(package, info);
    package.changedFiles = package.changedFiles || [];
    package.changedFiles.push(pathname);
    return package;
  }

  function getAllPackages() {
    return Array.from(store.values());
  }
  function getAllNoPackageFile() {
    return noPackageFiles;
  }
};

const main = async (needWrite = false) => {
  const isRelease = process.argv[2] === "from-release";
  const fn = !isRelease ? getChangedFileFromRange : getChangedFile;
  const changedFiles = await fn();
  if (changedFiles.length === 0) return;
  const store = createLibraryStore();
  await Promise.all(changedFiles.map((filename) => store.process(filename)));
  const packages = store.getAllPackages();
  const noPackageFiles = store.getAllNoPackageFile();
  const info = {
    packages,
    noPackageFiles,
    needJAVA: packages.filter((v) => v.type !== "f").length > 0,
  };

  if (needWrite) await fsp.writeFile(TEMP_FILE, JSON.stringify(info), "utf-8");
  return info;
};

module.exports = main;

if (path.basename(process.argv[1]) === "detect.js") {
  main(true);
}
