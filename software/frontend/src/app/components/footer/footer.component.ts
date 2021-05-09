import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'ucm-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent {
  constructor(private router: Router) {}

  isIndex(): boolean {
    return this.router.url === '/';
  }
}
