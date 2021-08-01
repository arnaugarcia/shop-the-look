import { Component, ViewEncapsulation } from '@angular/core';
import { fadeIn } from '@core/animations/core.animation';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'stl-content',
  templateUrl: './content.component.html',
  encapsulation: ViewEncapsulation.None,
  animations: [fadeIn],
})
export class ContentComponent {
  /**
   * Animation
   *
   * @param outlet
   */
  animation(outlet: RouterOutlet): any {
    return outlet.activatedRouteData.animation;
  }
}
