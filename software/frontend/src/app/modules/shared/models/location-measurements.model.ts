export interface LocationMeasurementsModel {
  entries: [
    {
      timestamp: string;
      values: { [id: number]: any };
    }
  ];
}
