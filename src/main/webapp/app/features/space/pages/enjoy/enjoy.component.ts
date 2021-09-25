import { Component } from '@angular/core';
import { StudioStore } from '../../store/studio.store';
import { snippetCode } from '../../../../../@core/components/card-snippet/card-snippet.component';

@Component({
  selector: 'stl-enjoy',
  templateUrl: './enjoy.component.html',
  styleUrls: ['./enjoy.component.scss'],
})
export class EnjoyComponent {
  public code = '';
  _snippetCodeSwiperNavigations: snippetCode = {
    html: `
  <div class="swiper-navigations swiper-container" [swiper]="swiperNavigations">
    <div class="swiper-wrapper">
      <div class="swiper-slide">
        <img class="img-fluid" src="assets/images/banner/banner-7.jpg" alt="banner">
      </div>
      <div class="swiper-slide">
        <img class="img-fluid" src="assets/images/banner/banner-4.jpg" alt="banner">
      </div>
      <div class="swiper-slide">
        <img class="img-fluid" src="assets/images/banner/banner-14.jpg" alt="banner">
      </div>
      <div class="swiper-slide">
        <img class="img-fluid" src="assets/images/banner/banner-3.jpg" alt="banner">
      </div>
      <div class="swiper-slide">
        <img class="img-fluid" src="assets/images/banner/banner-2.jpg" alt="banner">
      </div>
    </div>
    <!-- Add Arrows -->
    <div class="swiper-button-next"></div>
    <div class="swiper-button-prev"></div>
  </div>
  `,
    ts: `
  public swiperNavigations: SwiperConfigInterface = {
    navigation: true
  };
  `,
  };

  constructor(private studioStore: StudioStore) {
    studioStore.navigate('enjoy');
    this.code = `<pre prism class='dark'>
  var x = 1;
  var y = 2;
</pre>`;
  }
}
