export interface PageResult<T> {
  page: number;
  size: number;
  total: number;
  data: T[];
}
