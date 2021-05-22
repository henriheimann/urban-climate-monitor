export interface SensorMeasurementsModel {
  timestamp: string;
  values: { [id: string]: any };
}
