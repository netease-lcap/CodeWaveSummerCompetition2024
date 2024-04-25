const office_front_reg = /^(frontend\/\S+?\/)/;
const normal_reg = /^(\S+?\/)/;

const not_allowed = {
  'script/': true,
  '.github/': true,
  'packages/': true,
};

const regs = [office_front_reg, normal_reg];

module.exports.getPackageRoot = (pathname) => {
  for (const reg of regs) {
    const result = reg.exec(pathname);
    if (result) {
      const packageRoot = result[1];
      if (not_allowed[packageRoot]) return null;
      return packageRoot;
    }
  }
  return null;
};
