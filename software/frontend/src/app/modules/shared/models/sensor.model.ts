export interface SensorModel {
  id: number | null;
  name: string;
  ttnId: string | null;
  locationId: number | null;
  position: number[];
  rotation: number[];
  measurements: { [id: string]: any } | null;
}
