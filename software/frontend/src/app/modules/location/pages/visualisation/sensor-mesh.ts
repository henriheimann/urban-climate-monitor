import * as THREE from 'three';
import { LocationSensor } from '../../../shared/models/location-sensor.model';

export class SensorMesh extends THREE.Mesh {
  constructor(private locationSensor: LocationSensor, onClick: (sensorMesh: SensorMesh) => void) {
    super(new THREE.BoxGeometry(0.1, 0.1, 0.02), new THREE.MeshBasicMaterial({ color: 0x00ddee }));

    this.position.x = locationSensor.position[0];
    this.position.y = locationSensor.position[1];
    this.position.z = locationSensor.position[2];
  }

  public getLocationSensor(): LocationSensor {
    return this.locationSensor;
  }
}
