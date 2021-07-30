import { Component, ElementRef, Inject, OnDestroy, OnInit, Renderer2, RendererFactory2, ViewEncapsulation } from '@angular/core';
import { CoreConfigService } from '../../../@core/services/config.service';
import { CoreLoadingScreenService } from '../../../@core/services/loading-screen.service';
import { Subject } from 'rxjs';
import { DOCUMENT } from '@angular/common';
import { takeUntil } from 'rxjs/operators';
import { CoreConfig } from '../../../@core/types';
import { Title } from '@angular/platform-browser';
import { LangChangeEvent, TranslateService } from '@ngx-translate/core';
import * as dayjs from 'dayjs';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

@Component({
  selector: 'stl-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class MainComponent implements OnInit, OnDestroy {
  public coreConfig: CoreConfig = new CoreConfig();

  private renderer: Renderer2;
  private _unsubscribeAll: Subject<any>;

  constructor(
    @Inject(DOCUMENT) private document: any,
    private titleService: Title,
    private elementRef: ElementRef,
    private router: Router,
    public coreConfigService: CoreConfigService,
    private coreLoadingScreenService: CoreLoadingScreenService,
    private translateService: TranslateService,
    rootRenderer: RendererFactory2
  ) {
    // Set the private defaults
    this._unsubscribeAll = new Subject();
    this.renderer = rootRenderer.createRenderer(document.querySelector('html'), null);
    this.elementRef.nativeElement.classList.add('vertical-layout', 'vertical-menu-modern', 'navbar-floating', 'footer-static');
  }

  /**
   * On init
   */
  ngOnInit(): void {
    // Subscribe to config changes
    this.coreConfigService.config.pipe(takeUntil(this._unsubscribeAll)).subscribe(config => {
      this.coreConfig = config;

      // Blank layout
      this.applyLayoutLogic();
    });

    this.translateService.onLangChange.subscribe((langChangeEvent: LangChangeEvent) => {
      this.updateTitle();
      dayjs.locale(langChangeEvent.lang);
      this.renderer.setAttribute(document.querySelector('html'), 'lang', langChangeEvent.lang);
    });
  }

  ngOnDestroy(): void {
    // Unsubscribe from all subscriptions
    this._unsubscribeAll.next();
    this._unsubscribeAll.complete();
  }

  private getPageTitle(routeSnapshot: ActivatedRouteSnapshot): string {
    let title: string = routeSnapshot.data['pageTitle'] ?? '';
    if (routeSnapshot.firstChild) {
      title = this.getPageTitle(routeSnapshot.firstChild) || title;
    }
    return title;
  }

  private updateTitle(): void {
    let pageTitle = this.getPageTitle(this.router.routerState.snapshot.root);
    if (!pageTitle) {
      pageTitle = 'global.title';
    }
    this.translateService.get(pageTitle).subscribe(title => this.titleService.setTitle(title));
  }

  private applyLayoutLogic(): void {
    if (this.coreConfig.layout.menu.hidden && this.coreConfig.layout.navbar.hidden && this.coreConfig.layout.footer.hidden) {
      this.elementRef.nativeElement.classList.add('blank-page');
      // ! Fix: Transition issue while coming from blank page
      this.renderer.setAttribute(this.elementRef.nativeElement.getElementsByClassName('app-content')[0], 'style', 'transition:none');
    } else {
      this.elementRef.nativeElement.classList.remove('blank-page');
      // ! Fix: Transition issue while coming from blank page
      setTimeout(() => {
        this.renderer.setAttribute(
          this.elementRef.nativeElement.getElementsByClassName('app-content')[0],
          'style',
          'transition:300ms ease all'
        );
      }, 0);
    }

    // Skin Class (Adding to body as it requires highest priority)
    this.document.body.classList.remove('default-layout', 'dark-layout');
    this.document.body.classList.add(this.coreConfig.layout.skin + '-layout');
  }
}
