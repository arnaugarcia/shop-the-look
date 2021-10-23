import { Component } from '@angular/core';
import { snippetCode } from '../../../../../@core/components/card-snippet/card-snippet.component';

@Component({
  selector: 'stl-api-key',
  templateUrl: './api-key.component.html',
  styleUrls: ['./api-key.component.scss'],
})
export class ApiKeyComponent {
  snippetCode?: snippetCode;

  constructor() {
    this.snippetCode = {
      html: `<script async defer src='https://cdn.shopthelook.com/client/shopthelook.js?v=1.0&key=123412341234'></script>`,
      isCollapsed: false,
    };
  }
}
