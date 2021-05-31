import * as THREE from 'three';
import { Color, Shader, Vector3 } from 'three';

export class LocationMaterial extends THREE.MeshPhongMaterial {
  public static MAX_SENSORS = 10;

  private shader: Shader | undefined;
  private savedUniformValues: Array<{ position: Vector3; color: Color }> | undefined;

  constructor() {
    super();

    this.onBeforeCompile = (shader) => {
      const sensorDataValues = [];
      for (let i = 0; i < LocationMaterial.MAX_SENSORS; i++) {
        sensorDataValues.push({
          position: new Vector3(0, 0, 0),
          color: new Vector3(0, 0, 0)
        });
      }

      shader.uniforms['sensorData'] = {
        value: sensorDataValues
      };
      shader.uniforms['sensorCount'] = {
        value: 0
      };

      shader.vertexShader = `#define DISTANCE\nvarying vec3 vWorldPosition;\n` + shader.vertexShader;
      shader.vertexShader = shader.vertexShader.replace(
        '#include <envmap_vertex>',
        `#include <envmap_vertex>\nvWorldPosition = worldPosition.xyz;\n`
      );

      shader.fragmentShader =
        `
        struct SensorData {
          vec3 position;
          vec3 color;
        };
        uniform SensorData sensorData[${LocationMaterial.MAX_SENSORS}];
        uniform int sensorCount;

        varying vec3 vWorldPosition;
        ` + shader.fragmentShader;

      shader.fragmentShader = shader.fragmentShader.replace(
        `vec4 diffuseColor = vec4( diffuse, opacity );`,
        `
        vec3 accumulatedColor = diffuse;
        for (int i = 0 ; i < sensorCount; ++i) {
          vec3 difference = vWorldPosition - sensorData[i].position;
          float distance = length(difference);
          float distanceClamped = 1.0f - clamp(distance * 0.35f, 0.0f, 1.0f);
          accumulatedColor = mix(accumulatedColor, sensorData[i].color, distanceClamped) + sensorData[i].color * distanceClamped;
        }
        vec4 diffuseColor = vec4(diffuse * accumulatedColor, opacity);
        `
      );

      this.shader = shader;

      if (this.savedUniformValues) {
        this.updateUniforms(this.savedUniformValues);
      }
    };
  }

  public updateUniforms(values: Array<{ position: Vector3; color: Color }>): void {
    if (values.length > 10) {
      throw new Error(`Passed ${values.length} uniform values to LocationMaterial, only ${LocationMaterial.MAX_SENSORS} are allowed`);
    }

    if (this.shader !== undefined) {
      values.forEach((value, index) => {
        if (this.shader !== undefined) {
          this.shader.uniforms['sensorData'].value[index] = value;
        }
      });
      this.shader.uniforms['sensorCount'].value = values.length;
    }

    this.savedUniformValues = values;
  }
}
