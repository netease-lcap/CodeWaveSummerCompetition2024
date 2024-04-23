const { exec } = require('child_process');

const execCommand = async (
  commandStr,
  { throwWhenStderr = false, ...options } = {},
) => {
  return new Promise((res, rej) => {
    exec(commandStr, options, (err, stdout, stderr) => {
      console.log('===>', commandStr, options, stdout);
      if (err) {
        stderr && console.error(commandStr, stderr);
        rej(err);
        return;
      }
      if (throwWhenStderr && stderr) {
        stderr && console.error(commandStr, stderr);
        rej(new Error(stderr));
        return;
      }
      stderr && console.error(commandStr, stderr);

      res(stdout);
    });
  });
};

const execCommands = (commands, options) => {
  return commands.reduce(async (result, command) => {
    await result;
    await execCommand(command, options);
  }, Promise.resolve());
};

module.exports = {
  execCommand,
  execCommands,
};
