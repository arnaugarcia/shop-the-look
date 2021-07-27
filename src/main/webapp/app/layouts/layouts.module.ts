import { NgModule } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { FooterModule } from './footer/footer.module';
import { ErrorComponent } from './error/error.component';
import { MainComponent } from './main/main.component';
import { PageRibbonComponent } from './profiles/page-ribbon.component';
import { NavbarModule } from './navbar/navbar.module';
import { MenuModule } from './menu/menu.module';
import { ContentModule } from './content/content.module';
import { ContentHeaderModule } from './content-header/content-header.module';
import { CoreSidebarModule } from '../../@core/components';
import { FlexLayoutModule } from '@angular/flex-layout';
import { CustomBreakPointsProvider } from './custom-breakpoints';

@NgModule({
  declarations: [ErrorComponent, MainComponent, PageRibbonComponent],
  imports: [
    SharedModule,
    CoreSidebarModule,
    MenuModule,
    ContentModule,
    ContentHeaderModule,
    FooterModule,
    NavbarModule,
    FlexLayoutModule.withConfig({ disableDefaultBps: true }),
  ],
  providers: [CustomBreakPointsProvider],
})
export class LayoutsModule {}
