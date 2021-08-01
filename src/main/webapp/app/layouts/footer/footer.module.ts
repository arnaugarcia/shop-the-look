import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { FooterComponent } from './footer.component';
import { ScrollTopComponent } from './scroll-to-top/scroll-top.component';

@NgModule({
  declarations: [FooterComponent, ScrollTopComponent],
  imports: [SharedModule],
  exports: [FooterComponent],
})
export class FooterModule {}
