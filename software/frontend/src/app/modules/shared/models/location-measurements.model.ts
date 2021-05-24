export interface LocationMeasurementsModel {
  entries: [
    {
      timestamp: string;
      measurements: {
        [sensor_id: number]: {
          [measurement_id: string]: any;
        };
      };
    }
  ];
}
