import { Component, ElementRef, NgZone, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { LocationService } from '../../../shared/services/location.service';
import { filter, map, withLatestFrom } from 'rxjs/operators';
import { ActivatedRoute } from '@angular/router';
import * as THREE from 'three';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls';
import { TransformControls } from 'three/examples/jsm/controls/TransformControls';
import { OBJLoader } from 'three/examples/jsm/loaders/OBJLoader';
import { environment } from '../../../../../environments/environment';
import { SensorMesh } from './meshes/sensor-mesh';
import { Store } from '@ngrx/store';
import { selectLocationState } from '../../store/location.selectors';
import { selectSensor, setModifiedPosition, setModifiedRotation } from '../../store/location.actions';
import { SensorModel } from '../../../shared/models/sensor.model';
import { LocationState } from '../../store/location.reducer';
import { Shader, Vector3 } from 'three';

@Component({
  selector: 'ucm-visualisation-page',
  templateUrl: './visualisation-page.component.html',
  styleUrls: ['./visualisation-page.component.css']
})
export class VisualisationPageComponent implements OnInit, OnDestroy {
  @ViewChild('canvasWrapper', { static: true })
  canvasWrapper!: ElementRef<HTMLDivElement>;

  @ViewChild('rendererCanvas', { static: true })
  rendererCanvas!: ElementRef<HTMLCanvasElement>;

  location$ = this.locationService.entityMap$.pipe(
    withLatestFrom(
      this.activatedRoute.params.pipe(
        filter((params) => params?.locationId != undefined),
        map((params) => params.locationId)
      )
    ),
    map(([entityMap, locationId]) => entityMap[locationId])
  );

  locationState$ = this.store.select(selectLocationState);
  locationState: LocationState | undefined;

  private wrapper!: HTMLDivElement;
  private canvas!: HTMLCanvasElement;
  private renderer!: THREE.WebGLRenderer;
  private camera!: THREE.PerspectiveCamera;
  private scene!: THREE.Scene;
  private orbitControls!: OrbitControls;
  private transformControls!: TransformControls;
  private raycaster!: THREE.Raycaster;

  private locationModelGroup: THREE.Group | undefined;
  private locationModelMaterial: THREE.MeshPhongMaterial | undefined;
  private sensorsGroup: THREE.Group | undefined;

  private frameId: number | null = null;

  private mouseDragged = false;

  constructor(
    private ngZone: NgZone,
    private locationService: LocationService,
    private activatedRoute: ActivatedRoute,
    private store: Store
  ) {}

  ngOnInit(): void {
    this.createScene(this.canvasWrapper, this.rendererCanvas);
    this.animate();

    this.locationState$.subscribe((state) => {
      this.updateLocationModelShaderUniforms(state);

      const previousOrCurrentSelectedSensor = state.selectedSensor || this.locationState?.selectedSensor;
      this.locationState = state;

      if (previousOrCurrentSelectedSensor != null) {
        const selectedSensorMesh = this.getSensorMeshForSensor(previousOrCurrentSelectedSensor);
        if (selectedSensorMesh && state.modifiedPosition) {
          selectedSensorMesh.position.x = state.modifiedPosition[0];
          selectedSensorMesh.position.y = state.modifiedPosition[1];
          selectedSensorMesh.position.z = state.modifiedPosition[2];
        }
        if (selectedSensorMesh && state.modifiedRotation) {
          selectedSensorMesh.rotation.x = state.modifiedRotation[0];
          selectedSensorMesh.rotation.y = state.modifiedRotation[1];
          selectedSensorMesh.rotation.z = state.modifiedRotation[2];
        }
      }

      if (state.selectedSensor != null) {
        const selectedSensorMesh = this.getSensorMeshForSensor(state.selectedSensor);
        if (selectedSensorMesh && state.editingMode != 'none') {
          this.transformControls.attach(selectedSensorMesh);
          this.transformControls.mode = state.editingMode;
          return;
        }
      }

      this.transformControls.detach();
    });
  }

  public ngOnDestroy(): void {
    if (this.frameId != null) {
      cancelAnimationFrame(this.frameId);
    }
  }

  public createScene(wrapper: ElementRef<HTMLDivElement>, canvas: ElementRef<HTMLCanvasElement>): void {
    this.wrapper = wrapper.nativeElement;
    this.canvas = canvas.nativeElement;

    this.renderer = new THREE.WebGLRenderer({
      canvas: this.canvas,
      alpha: true,
      antialias: true
    });
    this.renderer.setSize(this.wrapper.clientWidth, this.wrapper.clientHeight);

    this.scene = new THREE.Scene();

    this.camera = new THREE.PerspectiveCamera(75, this.wrapper.clientWidth / this.wrapper.clientHeight, 0.1, 1000);
    this.camera.position.z = 5;
    this.scene.add(this.camera);

    this.raycaster = new THREE.Raycaster();

    const ambientLight = new THREE.AmbientLight(0x888888);
    this.scene.add(ambientLight);

    const directionalLight = new THREE.DirectionalLight(0x888888);
    directionalLight.position.x = 5;
    directionalLight.position.y = 3;
    directionalLight.position.z = 4;
    this.scene.add(directionalLight);

    this.locationModelMaterial = new THREE.MeshPhongMaterial();
    this.locationModelMaterial.onBeforeCompile = (shader) => {
      shader.uniforms['sensorData'] = {
        value: [
          {
            position: new Vector3(0, 0, 0),
            color: new Vector3(0, 0, 0)
          },
          {
            position: new Vector3(0, 0, 0),
            color: new Vector3(0, 0, 0)
          },
          {
            position: new Vector3(0, 0, 0),
            color: new Vector3(0, 0, 0)
          },
          {
            position: new Vector3(0, 0, 0),
            color: new Vector3(0, 0, 0)
          },
          {
            position: new Vector3(0, 0, 0),
            color: new Vector3(0, 0, 0)
          },
          {
            position: new Vector3(0, 0, 0),
            color: new Vector3(0, 0, 0)
          },
          {
            position: new Vector3(0, 0, 0),
            color: new Vector3(0, 0, 0)
          },
          {
            position: new Vector3(0, 0, 0),
            color: new Vector3(0, 0, 0)
          },
          {
            position: new Vector3(0, 0, 0),
            color: new Vector3(0, 0, 0)
          },
          {
            position: new Vector3(0, 0, 0),
            color: new Vector3(0, 0, 0)
          }
        ]
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
        uniform SensorData sensorData[10];
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

      if (this.locationModelMaterial) {
        this.locationModelMaterial.userData.shader = shader;
      }
    };

    this.orbitControls = new OrbitControls(this.camera, this.renderer.domElement);
    this.transformControls = new TransformControls(this.camera, this.renderer.domElement);
    this.transformControls.addEventListener('dragging-changed', (event) => {
      this.orbitControls.enabled = !event.value;
    });

    this.transformControls.addEventListener('change', () => {
      this.onTransformControlsChanged();
    });
    //this.transformControls.setTranslationSnap(0.1);
    //this.transformControls.setRotationSnap(1);

    this.scene.add(this.transformControls);

    this.location$.subscribe((location) => {
      if (this.locationModelGroup) {
        this.scene.remove(this.locationModelGroup);
      }
      if (this.sensorsGroup) {
        this.scene.remove(this.sensorsGroup);
      }

      // TODO: Dont reload OBJ if url has not changed
      if (location) {
        const loader = new OBJLoader();
        loader.loadAsync(environment.backendUrl + location.model3d.url).then((group) => {
          group.traverse((object) => {
            if (object instanceof THREE.Mesh) {
              object.material = this.locationModelMaterial;
            }
          });
          this.locationModelGroup = group;
          this.scene.add(this.locationModelGroup);
        });

        if (this.locationState) {
          this.updateLocationModelShaderUniforms(this.locationState);
        }

        const sensorMeshes = location.sensors.map((sensor) => new SensorMesh(sensor));

        this.sensorsGroup = new THREE.Group();
        this.sensorsGroup.add(...sensorMeshes);
        this.scene.add(this.sensorsGroup);
      }
    });
  }

  public animate(): void {
    this.ngZone.runOutsideAngular(() => {
      if (document.readyState !== 'loading') {
        this.render();
      } else {
        window.addEventListener('DOMContentLoaded', () => {
          this.render();
        });
      }

      window.addEventListener('resize', () => {
        this.resize();
      });
    });
  }

  public render(): void {
    this.frameId = requestAnimationFrame(() => {
      this.render();
    });

    this.renderer.render(this.scene, this.camera);
  }

  public resize(): void {
    const width = this.wrapper.clientWidth;
    const height = this.wrapper.clientHeight;

    this.camera.aspect = width / height;
    this.camera.updateProjectionMatrix();

    this.renderer.setSize(width, height);
  }

  private updateLocationModelShaderUniforms(state: LocationState): void {
    if (state.loadedMeasurements != undefined) {
      const measurements = state.loadedMeasurements.entries[state.selectedMeasurementsIndex].measurements;

      const sensorData = [];

      const getColor = (measurement: number) => {
        const c1 = new THREE.Color('#df3615');
        const c2 = new THREE.Color('#ee732e');
        const c3 = new THREE.Color('#48aaab');
        const c4 = new THREE.Color('#048399');

        const min = state.loadedMeasurementsMin ? state.loadedMeasurementsMin[state.selectedMeasurementsType] : 0;
        const max = state.loadedMeasurementsMax ? state.loadedMeasurementsMax[state.selectedMeasurementsType] : 0;

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
      };

      if (this.sensorsGroup?.children) {
        for (const child of this.sensorsGroup.children) {
          if (child instanceof SensorMesh) {
            const id = child.getSensor().id;
            if (id != null) {
              let measurementsForSensor;
              if (measurements[id]) {
                measurementsForSensor = measurements[id][state.selectedMeasurementsType];
              } else {
                measurementsForSensor = undefined;
              }

              if (measurementsForSensor) {
                sensorData.push({
                  position: child.position,
                  color: getColor(measurementsForSensor)
                });
              }
            }
          }
        }
      }

      if (this.locationModelMaterial) {
        const shader = this.locationModelMaterial.userData.shader;
        if (shader) {
          sensorData.forEach((value, index) => {
            shader.uniforms['sensorData'].value[index] = value;
          });
          shader.uniforms['sensorCount'].value = sensorData.length;
        }
      }
    }
  }

  private getSensorMeshForSensor(sensor: SensorModel): SensorMesh | undefined {
    if (this.sensorsGroup?.children) {
      return this.sensorsGroup.children.find((object3d) => object3d instanceof SensorMesh && object3d.getSensor() == sensor) as SensorMesh;
    } else {
      return undefined;
    }
  }

  onCanvasMouseUp($event: MouseEvent): void {
    if (!this.mouseDragged) {
      const mouse = new THREE.Vector2();
      mouse.x = ($event.offsetX / this.canvas.width) * 2 - 1;
      mouse.y = -($event.offsetY / this.canvas.height) * 2 + 1;
      this.raycaster.setFromCamera(mouse, this.camera);

      if (this.sensorsGroup?.children) {
        const intersects = this.raycaster.intersectObjects(this.sensorsGroup.children, false);
        if (intersects.length > 0) {
          const firstIntersectObject = intersects[0].object;
          if (firstIntersectObject instanceof SensorMesh) {
            this.store.dispatch(selectSensor({ sensor: firstIntersectObject.getSensor() }));
          }
        } else if (this.locationState?.editingMode == 'none') {
          this.store.dispatch(selectSensor({ sensor: null }));
        }
      }
    }
  }

  private onTransformControlsChanged(): void {
    if (this.locationState?.selectedSensor) {
      const sensorMesh = this.getSensorMeshForSensor(this.locationState?.selectedSensor);
      if (sensorMesh) {
        if (this.locationState.editingMode == 'translate') {
          this.store.dispatch(setModifiedPosition({ position: [sensorMesh.position.x, sensorMesh.position.y, sensorMesh.position.z] }));
        } else if (this.locationState.editingMode == 'rotate') {
          this.store.dispatch(setModifiedRotation({ rotation: [sensorMesh.rotation.x, sensorMesh.rotation.y, sensorMesh.rotation.z] }));
        }
      }
    }
  }

  onCanvasMouseDown(): void {
    this.mouseDragged = false;
  }

  onCanvasMouseMove(): void {
    this.mouseDragged = true;
  }
}
