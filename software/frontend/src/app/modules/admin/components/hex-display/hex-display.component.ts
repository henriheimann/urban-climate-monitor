import { Component, Input } from '@angular/core';

@Component({
  selector: 'ucm-hex-output',
  templateUrl: './hex-display.component.html',
  styleUrls: ['./hex-display.component.css']
})
export class HexDisplayComponent {
  hexString: string | undefined;

  cString: string | undefined;

  @Input() name: string | undefined;

  @Input()
  set hex(hexString: string) {
    this.hexString = hexString;
    this.cString = `{${hexString
      .match(/.{1,2}/g)
      ?.map((str) => `0x${str}`)
      .join(', ')}}`;
  }

  constructor() {}
}
