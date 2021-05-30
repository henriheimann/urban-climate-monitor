import { Component, Input } from '@angular/core';

@Component({
  selector: 'ucm-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent {
  @Input()
  lightTextColor = false;

  @Input()
  absolutePosition = false;
}
