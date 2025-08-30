import request from '../request';
export function register(data: { username: string; password: string; email: string }) {
  return request.post('/auth/register', data);
}
