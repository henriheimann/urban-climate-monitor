import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {VisualisationService} from './visualisation.service';

@Component({
  selector: 'ucm-visualisation',
  templateUrl: './visualisation.component.html',
  styleUrls: ['./visualisation.component.scss']
})
export class VisualisationComponent implements OnInit {

  @ViewChild('canvasWrapper', {static: true})
  canvasWrapper!: ElementRef<HTMLDivElement>;

  @ViewChild('rendererCanvas', {static: true})
  rendererCanvas!: ElementRef<HTMLCanvasElement>;

  public constructor(private visualisationService: VisualisationService) {
  }

  public ngOnInit(): void {
    this.visualisationService.createScene(this.canvasWrapper, this.rendererCanvas);
    this.visualisationService.animate();
  }
}
