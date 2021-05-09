import { Upload } from './upload.model';

export interface Location {
  id: number | null;
  name: string;
  icon: Upload;
  model3d: Upload;
  iconUrl: string | null;
  model3dUrl: string | null;
}
