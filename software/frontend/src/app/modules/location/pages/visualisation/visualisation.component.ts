import { Component, ElementRef, NgZone, OnDestroy, OnInit, ViewChild } from '@angular/core';
import * as THREE from 'three';
import { OBJLoader } from 'three/examples/jsm/loaders/OBJLoader';
import { Location } from '../../../shared/models/location.model';
import { Observable } from 'rxjs';
import { environment } from '../../../../../environments/environment';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls';
import { LocationService } from '../../services/location.service';
import { LocationSensor } from '../../../shared/models/location-sensor.model';
import { TransformControls } from 'three/examples/jsm/controls/TransformControls';
import { SensorMesh } from './sensor-mesh';

@Component({
  selector: 'ucm-visualisation',
  templateUrl: './visualisation.component.html',
  styleUrls: ['./visualisation.component.css']
})
export class VisualisationComponent implements OnInit, OnDestroy {
  @ViewChild('canvasWrapper', { static: true })
  canvasWrapper!: ElementRef<HTMLDivElement>;

  @ViewChild('rendererCanvas', { static: true })
  rendererCanvas!: ElementRef<HTMLCanvasElement>;

  location$: Observable<Location>;

  sensors$: Observable<LocationSensor[]>;

  selectedSensorMesh: SensorMesh | undefined;

  private wrapper!: HTMLDivElement;

  private canvas!: HTMLCanvasElement;

  private renderer!: THREE.WebGLRenderer;

  private camera!: THREE.PerspectiveCamera;

  private scene!: THREE.Scene;

  private orbitControls!: OrbitControls;

  private transformControls!: TransformControls;

  originalPosition: number[] | undefined;

  originalRotation: number[] | undefined;

  editedPosition: number[] | undefined;

  editedRotation: number[] | undefined;

  private objGroup: THREE.Group | undefined;

  private frameId: number | null = null;

  constructor(private ngZone: NgZone, private locationService: LocationService) {
    this.location$ = this.locationService.getCurrentRouteLocation();
    this.sensors$ = this.locationService.getCurrentRouteLocationSensors();
  }

  ngOnInit(): void {
    this.createScene(this.canvasWrapper, this.rendererCanvas);
    this.animate();
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

    const ambientLight = new THREE.AmbientLight(0x222222);
    this.scene.add(ambientLight);

    const directionalLight = new THREE.DirectionalLight(0xffffff);
    directionalLight.position.x = 5;
    directionalLight.position.y = 5;
    directionalLight.position.z = 5;
    this.scene.add(directionalLight);

    this.orbitControls = new OrbitControls(this.camera, this.renderer.domElement);
    this.transformControls = new TransformControls(this.camera, this.renderer.domElement);
    this.transformControls.addEventListener('dragging-changed', (event) => {
      this.orbitControls.enabled = !event.value;
    });

    this.transformControls.addEventListener('change', () => this.updatePositionAndRotationVariables());
    this.transformControls.setTranslationSnap(0.1);
    this.transformControls.setRotationSnap(1);

    this.scene.add(this.transformControls);

    this.location$.subscribe((location) => {
      const loader = new OBJLoader();
      loader.loadAsync(environment.backendUrl + location.model3dUrl).then((group) => {
        this.objGroup = group;
        this.scene.add(group);
      });
    });

    this.sensors$.subscribe((sensors) => {
      sensors.forEach((sensor) =>
        this.scene.add(new SensorMesh(sensor, (sensorMesh) => this.onSensorMeshClick(sensorMesh)))
      );
    });
  }

  private onSensorMeshClick(sensorMesh: SensorMesh): void {
    this.selectedSensorMesh = sensorMesh;
    this.updateOriginalPositionAndRotationVariables();
    this.updatePositionAndRotationVariables();
  }

  onSensorPopupClose(): void {
    this.selectedSensorMesh = undefined;
  }

  onEditingModeChange(editingMode: string): void {
    if (this.selectedSensorMesh && editingMode !== 'none') {
      this.transformControls.attach(this.selectedSensorMesh);
      this.transformControls.mode = editingMode;
    } else {
      this.transformControls.detach();
    }
  }

  updatePosition(position: number[]): void {
    if (this.selectedSensorMesh) {
      this.selectedSensorMesh.position.x = position[0];
      this.selectedSensorMesh.position.y = position[1];
      this.selectedSensorMesh.position.z = position[2];
    }
  }

  updateRotation(rotation: number[]): void {
    if (this.selectedSensorMesh) {
      this.selectedSensorMesh.rotation.x = rotation[0];
      this.selectedSensorMesh.rotation.y = rotation[1];
      this.selectedSensorMesh.rotation.z = rotation[2];
    }
  }

  saveChanges(): void {
    if (this.selectedSensorMesh?.getLocationSensor() && this.editedPosition && this.editedRotation) {
      const locationSensor = this.selectedSensorMesh.getLocationSensor();
      locationSensor.position = this.editedPosition;
      locationSensor.rotation = this.editedPosition;
      this.locationService.updateCurrentRouteLocationSensor(locationSensor).subscribe();
      this.originalPosition = this.editedPosition;
      this.originalRotation = this.editedRotation;
    }
  }

  revertChanges(): void {
    this.editedPosition = this.originalPosition;
    this.editedRotation = this.originalRotation;
    if (this.editedPosition && this.editedRotation) {
      this.updatePosition(this.editedPosition);
      this.updateRotation(this.editedRotation);
    }
  }

  updatePositionAndRotationVariables(): void {
    if (this.selectedSensorMesh) {
      this.editedPosition = [
        this.selectedSensorMesh.position.x,
        this.selectedSensorMesh.position.y,
        this.selectedSensorMesh.position.z
      ];
      this.editedRotation = [
        this.selectedSensorMesh.rotation.x,
        this.selectedSensorMesh.rotation.y,
        this.selectedSensorMesh.rotation.z
      ];
    }
  }

  updateOriginalPositionAndRotationVariables(): void {
    if (this.selectedSensorMesh) {
      this.originalPosition = [
        this.selectedSensorMesh.position.x,
        this.selectedSensorMesh.position.y,
        this.selectedSensorMesh.position.z
      ];
      this.originalRotation = [
        this.selectedSensorMesh.rotation.x,
        this.selectedSensorMesh.rotation.y,
        this.selectedSensorMesh.rotation.z
      ];
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

    this.renderer.render(this.scene, this.camera);
  }

  public resize(): void {
    const width = this.wrapper.clientWidth;
    const height = this.wrapper.clientHeight;

    this.camera.aspect = width / height;
    this.camera.updateProjectionMatrix();

    this.renderer.setSize(width, height);
  }
}
