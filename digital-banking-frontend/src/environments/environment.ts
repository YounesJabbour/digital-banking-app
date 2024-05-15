import Config from '../utils/constants';

export const environment = {
  production: false,
  baseUrl: `${Config.API_URL}/${Config.API_VERSION}`,
};
