import * as THREE from 'three';
import { SensorModel } from '../../../../shared/models/sensor.model';

export class SensorMesh extends THREE.Mesh {
  constructor(private sensorModel: SensorModel) {
    super(new THREE.BoxGeometry(0.1, 0.1, 0.02), new THREE.MeshPhongMaterial({ color: 0xffffff }));

    this.position.x = sensorModel.position[0];
    this.position.y = sensorModel.position[1];
    this.position.z = sensorModel.position[2];

    this.rotation.x = sensorModel.rotation[0];
    this.rotation.y = sensorModel.rotation[1];
    this.rotation.z = sensorModel.rotation[2];
  }

  public getSensor(): SensorModel {
    return this.sensorModel;
  }
}
