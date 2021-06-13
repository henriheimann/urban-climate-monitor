import { Injectable } from '@angular/core';
import * as THREE from 'three';

@Injectable({
  providedIn: 'root'
})
export class MeasurementColorsService {
  public mapValueToCurveInverse(transformedMeasurement: number, min: number, max: number): number {
    if (transformedMeasurement == min) {
      return min;
    }

    const diff = max - min;
    const curvature = diff * 100;
    const x = transformedMeasurement;

    return -(curvature * x) / (diff * (x - diff)) + min;
  }

  public mapValueToCurve(measurement: number, min: number, max: number): number {
    if (measurement == min) {
      return min;
    }

    const diff = max - min;
    const curvature = diff * 100;
    const x = measurement - min;

    return -curvature / (x + curvature / diff) + diff;
  }

  public getColor(measurement: number, min: number, max: number, curveMapped: boolean = false): THREE.Color {
    const c1 = new THREE.Color('#df3615');
    const c2 = new THREE.Color('#ee732e');
    const c3 = new THREE.Color('#48aaab');
    const c4 = new THREE.Color('#048399');

    if (curveMapped) {
      measurement = this.mapValueToCurve(measurement, min, max);
    }

    const c1Value = max;
    const c2Value = min + ((max - min) * 2.0) / 3.0;
    const c3Value = min + (max - min) / 3.0;
    const c4Value = min;

    const color = new THREE.Color();

    const mapValueTo0To1Range = (value: number, lowerBound: number, upperBound: number) => {
      return (value - lowerBound) / (upperBound - lowerBound);
    };

    if (measurement < c3Value) {
      color.lerpColors(c4, c3, mapValueTo0To1Range(measurement, c4Value, c3Value));
    } else if (measurement < c2Value) {
      color.lerpColors(c3, c2, mapValueTo0To1Range(measurement, c3Value, c2Value));
    } else {
      color.lerpColors(c2, c1, mapValueTo0To1Range(measurement, c2Value, c1Value));
    }

    return color;
  }
}
