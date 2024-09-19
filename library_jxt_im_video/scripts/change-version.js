const { writeFileSync } = require('fs');
const packageJson = require('../package.json');

const version = packageJson.version;

const versionArray = version.split('.');

// 修订版本号
let lastVersion = versionArray.pop();
lastVersion++;

versionArray.push(lastVersion);

packageJson.version = versionArray.join('.');

writeFileSync('./package.json', JSON.stringify(packageJson, null, 4), 'utf-8');
