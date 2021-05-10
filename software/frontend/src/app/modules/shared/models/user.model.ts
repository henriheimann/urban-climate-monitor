export interface UserModel {
  username: string;
  role: string;
  locationsWithPermission: number[];
  password?: string;
}
