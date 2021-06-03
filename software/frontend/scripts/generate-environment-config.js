const { writeFile } = require('fs');

const targetPath = './src/environments/environment.ts';

const envConfigFile = `export const environment = {
  production: ${process.env.UCM_FRONTEND_PRODUCTION},
  backendUrl: '${process.env.UCM_BACKEND_URL}',
  oauthClientLogin: '${process.env.UCM_BACKEND_OAUTH_CLIENT_ID}:${process.env.UCM_BACKEND_OAUTH_CLIENT_SECRET}'
};
`;

console.log('The file `environment.ts` will be written with the following content: \n');
console.log(envConfigFile);

writeFile(targetPath, envConfigFile, function (err) {
  if (err) {
    throw err;
  } else {
    console.log(`Angular environment.ts file generated correctly at ${targetPath} \n`);
  }
});
