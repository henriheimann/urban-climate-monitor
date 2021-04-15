export interface User {
  username: string;
  role: string;
  locationsWithPermission: number[];
  password?: string;
}
