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
import { EffectComposer } from 'three/examples/jsm/postprocessing/EffectComposer';
import { RenderPass } from 'three/examples/jsm/postprocessing/RenderPass';
import { SAOPass } from 'three/examples/jsm/postprocessing/SAOPass';
import { LocationMaterial } from './shaders/location-material';
import { LocationModel } from '../../../shared/models/location.model';
import { MeasurementColorsService } from '../../services/measurement-colors.service';
import { MeasurementsType } from '../../models/measurements-type';

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
  private composer!: EffectComposer;
  private camera!: THREE.PerspectiveCamera;
  private scene!: THREE.Scene;
  private orbitControls!: OrbitControls;
  private transformControls!: TransformControls;
  private raycaster = new THREE.Raycaster();
  private objLoader = new OBJLoader();
  private clock = new THREE.Clock();

  private loadedLocationModelUrl: string | undefined;
  private locationModelGroup: THREE.Group | undefined;
  private locationModelMaterial = new LocationMaterial();

  private sensorsGroup: THREE.Group | undefined;

  private frameId: number | null = null;
  private elapsedTime = 0;

  private mouseDragged = false;

  constructor(
    private ngZone: NgZone,
    private locationService: LocationService,
    private activatedRoute: ActivatedRoute,
    private store: Store,
    private measurementColorsService: MeasurementColorsService
  ) {}

  ngOnInit(): void {
    this.createScene(this.canvasWrapper, this.rendererCanvas);
    this.animate();

    // Must be called to prevent right white space
    setTimeout(() => this.resize(), 0);

    this.location$.subscribe((location) => this.onLocationChange(location));
    this.locationState$.subscribe((state) => this.onLocationStateChange(state));
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
    //this.renderer.setPixelRatio(window.devicePixelRatio);

    this.scene = new THREE.Scene();

    this.camera = new THREE.PerspectiveCamera(75, this.wrapper.clientWidth / this.wrapper.clientHeight, 0.1, 1000);
    this.camera.position.set(12, 12, 8);
    this.scene.add(this.camera);

    const ambientLight = new THREE.AmbientLight(0xaaaaaa);
    this.scene.add(ambientLight);

    const directionalLight = new THREE.DirectionalLight(0x666666);
    directionalLight.position.x = 5;
    directionalLight.position.y = 3;
    directionalLight.position.z = 4;
    this.scene.add(directionalLight);

    this.orbitControls = new OrbitControls(this.camera, this.renderer.domElement);
    this.transformControls = new TransformControls(this.camera, this.renderer.domElement);
    this.transformControls.addEventListener('dragging-changed', (event) => {
      this.orbitControls.enabled = !event.value;
    });

    this.transformControls.addEventListener('change', () => {
      this.onTransformControlsChanged();
    });

    this.scene.add(this.transformControls);

    this.composer = new EffectComposer(this.renderer);
    const renderPass = new RenderPass(this.scene, this.camera);
    this.composer.addPass(renderPass);
    this.enableSAO(true);
  }

  private enableSAO(enable: boolean): void {
    if (enable && this.composer.passes.length == 1) {
      const saoPass = new SAOPass(this.scene, this.camera, false, true);
      saoPass.params.saoScale = 300.0;
      saoPass.params.saoIntensity = 0.2;
      saoPass.params.saoBlur = 0.5;
      this.composer.addPass(saoPass);
    } else if (!enable && this.composer.passes.length == 2) {
      this.composer.removePass(this.composer.passes[1]);
    }
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

    const delta = this.clock.getDelta();
    this.elapsedTime += delta;

    if (this.locationState?.selectedSensor) {
      const sensorMesh = this.getSensorMeshForSensor(this.locationState?.selectedSensor);
      if (sensorMesh) {
        const scale = Math.sin(this.elapsedTime * 4) * 0.2 + 1;
        sensorMesh.scale.set(scale, scale, scale);
      }
    }

    this.composer.render();
  }

  public resize(): void {
    const width = this.wrapper.clientWidth;
    const height = this.wrapper.clientHeight;

    this.camera.aspect = width / height;
    this.camera.updateProjectionMatrix();

    this.renderer.setSize(width, height);
    this.composer.setSize(width, height);
  }

  private onLocationStateChange(state: LocationState): void {
    this.updateLocationModelShaderUniforms(state);

    const previousSelectedSensor = this.locationState?.selectedSensor;
    const previousOrCurrentSelectedSensor = state.selectedSensor || previousSelectedSensor;
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

    if (previousSelectedSensor != null && state.selectedSensor) {
      const sensorMesh = this.getSensorMeshForSensor(state.selectedSensor);
      if (sensorMesh) {
        sensorMesh.scale.set(1, 1, 1);
      }
    }

    if (state.editingMode != 'none' && state.selectedSensor != null) {
      const selectedSensorMesh = this.getSensorMeshForSensor(state.selectedSensor);
      if (selectedSensorMesh) {
        this.transformControls.attach(selectedSensorMesh);
        this.transformControls.mode = state.editingMode;
      }
      this.enableSAO(false);
    } else {
      this.transformControls.detach();
      this.enableSAO(true);
    }
  }

  private onLocationChange(location: LocationModel | undefined): void {
    if (location === undefined) {
      return;
    }

    // Only load location model if URL has changed
    const locationModelUrl = environment.backendUrl + location.model3d.url;
    if (this.loadedLocationModelUrl != locationModelUrl) {
      this.loadedLocationModelUrl = locationModelUrl;
      this.objLoader.loadAsync(environment.backendUrl + location.model3d.url).then((group) => {
        // Remove old location model group
        if (this.locationModelGroup) {
          this.scene.remove(this.locationModelGroup);
        }

        // Apply location material to each object
        group.traverse((object) => {
          if (object instanceof THREE.Mesh) {
            object.material = this.locationModelMaterial;
          }
        });

        // Set new location model group
        this.locationModelGroup = group;
        this.scene.add(this.locationModelGroup);
      });
    }

    // Remove old sensors group
    if (this.sensorsGroup) {
      this.scene.remove(this.sensorsGroup);
    }

    // Add new sensors
    const sensorMeshes = location.sensors.map((sensor) => new SensorMesh(sensor));
    this.sensorsGroup = new THREE.Group();
    this.sensorsGroup.add(...sensorMeshes);
    this.scene.add(this.sensorsGroup);

    // If we already have an location state, update the shader uniforms
    if (this.locationState) {
      this.updateLocationModelShaderUniforms(this.locationState);
    }
  }

  private updateLocationModelShaderUniforms(state: LocationState): void {
    const uniformValues = [];

    if (state.loadedMeasurements != undefined && state.editingMode == 'none') {
      const measurementOfIndex = state.loadedMeasurements.entries[state.selectedMeasurementsIndex];
      if (measurementOfIndex) {
        const measurements = measurementOfIndex.measurements;

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

                if (measurementsForSensor !== undefined) {
                  uniformValues.push({
                    position: child.position,
                    color: this.measurementColorsService.getColor(
                      measurementsForSensor,
                      state.loadedMeasurementsMin![state.selectedMeasurementsType] || 0,
                      state.loadedMeasurementsMax![state.selectedMeasurementsType] || 0,
                      state.selectedMeasurementsType === MeasurementsType.BRIGHTNESS_CURRENT
                    )
                  });
                }
              }
            }
          }
        }
      }
    }

    this.locationModelMaterial.updateUniforms(uniformValues);
  }

  private getSensorMeshForSensor(sensor: SensorModel): SensorMesh | undefined {
    if (this.sensorsGroup?.children) {
      return this.sensorsGroup.children.find(
        (object3d) => object3d instanceof SensorMesh && object3d.getSensor().id == sensor.id
      ) as SensorMesh;
    } else {
      return undefined;
    }
  }

  onCanvasMouseUp($event: MouseEvent): void {
    if (!this.mouseDragged && this.locationState?.editingMode == 'none') {
      const mouse = new THREE.Vector2();
      mouse.x = ($event.offsetX / this.wrapper.clientWidth) * 2 - 1;
      mouse.y = -($event.offsetY / this.wrapper.clientHeight) * 2 + 1;
      this.raycaster.setFromCamera(mouse, this.camera);

      if (this.sensorsGroup?.children) {
        const intersects = this.raycaster.intersectObjects(this.sensorsGroup.children, false);
        if (intersects.length > 0) {
          const firstIntersectObject = intersects[0].object;
          if (firstIntersectObject instanceof SensorMesh) {
            this.store.dispatch(selectSensor({ sensor: firstIntersectObject.getSensor() }));
          }
        } else {
          this.store.dispatch(selectSensor({ sensor: null }));
        }
      }
    }
  }

  onTransformControlsChanged(): void {
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
