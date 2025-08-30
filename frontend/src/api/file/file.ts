import request from '../request';
export function getFiles(params: any) {
  return request.get('/files', { params });
}
export function uploadFile(data: FormData) {
  return request.post('/files/upload', data);
}
export function deleteFile(id: number) {
  return request.delete(`/files/${id}`);
}
