import axios from 'axios';
const instance = axios.create({ baseURL: '/api', timeout: 10000 });
instance.interceptors.request.use(config => {
  // TODO: 请求拦截
  return config;
});
instance.interceptors.response.use(response => {
  // TODO: 响应拦截
  return response;
});
export default instance;
