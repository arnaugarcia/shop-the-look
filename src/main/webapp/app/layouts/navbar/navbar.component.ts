import { Component, HostBinding, HostListener, OnDestroy, OnInit, ViewEncapsulation } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { CoreConfigService } from '../../../@core/services/config.service';
import { CoreMediaService } from '../../../@core/services/media.service';
import { CoreSidebarService } from '../../../@core/components/core-sidebar/core-sidebar.service';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { CoreConfig } from '../../../@core/types';
import { AccountService } from '../../core/auth/account.service';
import { SessionStorageService } from 'ngx-webstorage';
import { ProfileService } from '../profiles/profile.service';
import { Account } from 'app/core/auth/account.model';
import { LoginService } from '../../auth/login/login.service';

@Component({
  selector: 'stl-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class NavbarComponent implements OnInit, OnDestroy {
  public hiddenMenu = false;

  public coreConfig: CoreConfig | undefined;

  public account: Account | null = null;

  @HostBinding('class.navbar-static-style-on-scroll')
  public windowScrolled = false;

  // Private
  private _unsubscribeAll: Subject<any>;

  constructor(
    private loginService: LoginService,
    private translateService: TranslateService,
    private sessionStorageService: SessionStorageService,
    private accountService: AccountService,
    private profileService: ProfileService,
    private router: Router,
    private coreConfigService: CoreConfigService,
    private coreMediaService: CoreMediaService,
    private coreSidebarService: CoreSidebarService
  ) {
    this._unsubscribeAll = new Subject();
  }

  /**
   * On init
   */
  ngOnInit(): void {
    // Subscribe to the config changes
    this.coreConfigService.config.pipe(takeUntil(this._unsubscribeAll)).subscribe((config: CoreConfig) => {
      this.coreConfig = config;
      this.hiddenMenu = config.layout.menu.hidden;
    });
    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
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

  changeLanguage(languageKey: string): void {
    this.sessionStorageService.store('locale', languageKey);
    this.translateService.use(languageKey);
  }

  /**
   * Toggle sidebar open
   *
   * @param key
   */
  toggleSidebar(key: string): void {
    this.coreSidebarService.getSidebarRegistry(key).toggleOpen();
  }

  logout(): void {
    this.loginService.logout();
    this.router.navigate(['/auth/login']);
  }

  // Add .navbar-static-style-on-scroll on scroll using HostListener & HostBinding
  @HostListener('window:scroll', [])
  private onWindowScroll(): void {
    if (window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop > 100) {
      this.windowScrolled = true;
    } else if ((this.windowScrolled && window.pageYOffset) || document.documentElement.scrollTop || document.body.scrollTop < 10) {
      this.windowScrolled = false;
    }
  }
}
