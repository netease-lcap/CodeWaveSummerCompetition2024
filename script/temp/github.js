const { GITHUB_REPOSITORY, GITHUB_TOKEN, COMMIT_SHA } = require('./env');
const fetch = require('node-fetch');
const fsp = require('fs/promises');

const createTagAndRelease = async (tagName) => {
  const url = `https://api.github.com/repos/${GITHUB_REPOSITORY}/releases`;

  const { id } = await fetch(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${GITHUB_TOKEN}`,
      Accept: 'application/vnd.github.raw+json',
      'X-GitHub-Api-Version': '2022-11-28',
    },
    body: JSON.stringify({
      name: tagName,
      tag_name: tagName,
      target_commitish: COMMIT_SHA,
    }),
  }).then((v) => v.json());
  return id;
};

const uploadReleaseAssets = async (releaseID, files) => {
  for (const file of files) {
    const content = await fsp.readFile(file.filename);
    const url = `https://uploads.github.com/repos/${GITHUB_REPOSITORY}/releases/${releaseID}/assets?name=${encodeURIComponent(
      file.name || file.filename,
    )}`;
    await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/octet-stream',
        'Content-Length': content.length,
        Authorization: `Bearer ${GITHUB_TOKEN}`,
        Accept: 'application/vnd.github.raw+json',
        'X-GitHub-Api-Version': '2022-11-28',
      },
      body: content,
    });
  }
};

module.exports = {
  uploadReleaseAssets,
  createTagAndRelease,
};
