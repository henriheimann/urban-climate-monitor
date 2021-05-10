import { SensorModel } from './sensor.model';
import { UploadModel } from '../../upload/models/upload.model';

export interface LocationModel {
  id: number | null;
  name: string;
  icon: UploadModel;
  model3d: UploadModel;
  sensors: SensorModel[];
}
