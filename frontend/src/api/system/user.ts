import request from '../request';
export function getUsers() {
  return request.get('/users');
}
export function addUser(data: any) {
  return request.post('/users', data);
}
export function deleteUser(id: number) {
  return request.delete(`/users/${id}`);
}
