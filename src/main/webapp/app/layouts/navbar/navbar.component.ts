import { Component, HostBinding, HostListener, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { CoreConfigService } from '../../../@core/services/config.service';
import { CoreMediaService } from '../../../@core/services/media.service';
import { CoreSidebarService } from '../../../@core/components/core-sidebar/core-sidebar.service';
import { MediaObserver } from '@angular/flex-layout';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { CoreConfig } from '../../../@core/types';
import * as _ from 'lodash';

@Component({
  selector: 'stl-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit, OnDestroy {
  public hiddenMenu = false;

  public coreConfig: CoreConfig | undefined;
  public currentSkin = '';
  public prevSkin: string | null = '';

  public languageOptions: any;
  public navigation: any;
  public selectedLanguage: any;

  @HostBinding('class.fixed-top')
  public isFixed = false;

  @HostBinding('class.navbar-static-style-on-scroll')
  public windowScrolled = false;

  // Private
  private _unsubscribeAll: Subject<any>;

  /**
   * Constructor
   *
   * @param {Router} _router
   * @param {CoreConfigService} _coreConfigService
   * @param {CoreSidebarService} _coreSidebarService
   * @param {CoreMediaService} _coreMediaService
   * @param {MediaObserver} _mediaObserver
   * @param {TranslateService} _translateService
   */
  constructor(
    private _router: Router,
    private _coreConfigService: CoreConfigService,
    private _coreMediaService: CoreMediaService,
    private _coreSidebarService: CoreSidebarService,
    private _mediaObserver: MediaObserver,
    public _translateService: TranslateService
  ) {
    // Set the private defaults
    this._unsubscribeAll = new Subject();
  }

  // Add .navbar-static-style-on-scroll on scroll using HostListener & HostBinding
  @HostListener('window:scroll', [])
  onWindowScroll(): void {
    if (window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop > 100) {
      this.windowScrolled = true;
    } else if ((this.windowScrolled && window.pageYOffset) || document.documentElement.scrollTop || document.body.scrollTop < 10) {
      this.windowScrolled = false;
    }
  }

  /**
   * Toggle sidebar open
   *
   * @param key
   */
  toggleSidebar(key: string): void {
    this._coreSidebarService.getSidebarRegistry(key).toggleOpen();
  }

  /**
   * Toggle Dark Skin
   */
  toggleDarkSkin(): void {
    // Get the current skin
    this._coreConfigService
      .getConfig()
      .pipe(takeUntil(this._unsubscribeAll))
      .subscribe(config => {
        this.currentSkin = config.layout.skin;
      });

    // Toggle Dark skin with prevSkin skin
    this.prevSkin = localStorage.getItem('prevSkin');

    if (this.currentSkin === 'dark') {
      this._coreConfigService.setConfig({ layout: { skin: this.prevSkin ? this.prevSkin : 'default' } }, { emitEvent: true });
    } else {
      localStorage.setItem('prevSkin', this.currentSkin);
      this._coreConfigService.setConfig({ layout: { skin: 'dark' } }, { emitEvent: true });
    }
  }

  /**
   * Logout method
   */
  logout(): void {
    this._router.navigate(['/pages/authentication/login-v2']);
  }

  // Lifecycle Hooks
  // -----------------------------------------------------------------------------------------------------

  /**
   * On init
   */
  ngOnInit(): void {
    // Subscribe to the config changes
    this._coreConfigService.config?.pipe(takeUntil(this._unsubscribeAll)).subscribe((config: CoreConfig) => {
      this.coreConfig = config;
      this.hiddenMenu = config.layout.menu.hidden;
      this.currentSkin = config.layout.skin;
    });

    // Set the selected language from default languageOptions
    this.selectedLanguage = _.find(this.languageOptions, {
      id: this._translateService.currentLang,
    });
  }

  /**
   * On destroy
   */
  ngOnDestroy(): void {
    // Unsubscribe from all subscriptions
    this._unsubscribeAll.next();
    this._unsubscribeAll.complete();
  }

  /* inProduction?: boolean;
  isNavbarCollapsed = true;
  languages = LANGUAGES;
  openAPIEnabled?: boolean;
  version = '';
  account: Account | null = null;

  constructor(
    private loginService: LoginService,
    private translateService: TranslateService,
    private sessionStorageService: SessionStorageService,
    private accountService: AccountService,
    private profileService: ProfileService,
    private router: Router
  ) {
    if (VERSION) {
      this.version = VERSION.toLowerCase().startsWith('v') ? VERSION : 'v' + VERSION;
    }
  }

  ngOnInit(): void {
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });
    this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
  }

  changeLanguage(languageKey: string): void {
    this.sessionStorageService.store('locale', languageKey);
    this.translateService.use(languageKey);
  }

  collapseNavbar(): void {
    this.isNavbarCollapsed = true;
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  logout(): void {
    this.collapseNavbar();
    this.loginService.logout();
    this.router.navigate(['']);
  }

  toggleNavbar(): void {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  } */
}
