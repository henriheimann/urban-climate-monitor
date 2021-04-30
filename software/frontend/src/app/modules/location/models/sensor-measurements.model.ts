export interface SensorLatestMeasurements {
  id: number;
  name: string;
  measurements: { [id: string]: any };
}
