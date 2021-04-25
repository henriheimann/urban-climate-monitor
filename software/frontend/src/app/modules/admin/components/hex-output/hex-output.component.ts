import {Component, Input} from '@angular/core';

@Component({
  selector: 'ucm-hex-output',
  templateUrl: './hex-output.component.html',
  styleUrls: ['./hex-output.component.css']
})
export class HexOutputComponent {

  hexString: string | undefined;
  cString: string | undefined;

  @Input() name: string | undefined;

  @Input()
  set hex(hexString: string) {
    this.hexString = hexString;
    this.cString = '{' + hexString.match(/.{1,2}/g)?.map(str => '0x' + str).join(', ') + '}';
  }

  constructor() { }
}
