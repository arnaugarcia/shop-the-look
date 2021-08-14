import { Component, OnDestroy, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { Subject } from 'rxjs';
import { CoreConfigService } from '@core/services/config.service';
import { CoreSidebarService } from '@core/components/core-sidebar/core-sidebar.service';
import { NavigationEnd, Router } from '@angular/router';
import { PerfectScrollbarDirective } from 'ngx-perfect-scrollbar';
import { filter, take, takeUntil } from 'rxjs/operators';
import { CoreConfig } from '@core/types';
import { ProfileService } from '../profiles/profile.service';
import { AccountService } from '../../core/auth/account.service';
import { Account } from 'app/core/auth/account.model';

@Component({
  selector: 'stl-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class MenuComponent implements OnInit, OnDestroy {
  coreConfig: CoreConfig | undefined;
  isCollapsed = false;
  isScrolled = false;
  inProduction?: boolean;
  openAPIEnabled?: boolean;
  account: Account | null = null;

  public adminMenuCollapsed = true;
  public productsMenuCollapsed = true;
  public employeesMenuCollapsed = true;

  @ViewChild(PerfectScrollbarDirective, { static: false }) directiveRef: PerfectScrollbarDirective | undefined;

  private _unsubscribeAll: Subject<any>;

  constructor(
    private coreConfigService: CoreConfigService,
    private coreSidebarService: CoreSidebarService,
    private router: Router,
    private profileService: ProfileService,
    private accountService: AccountService
  ) {
    // Set the private defaults
    this._unsubscribeAll = new Subject();
  }

  ngOnInit(): void {
    // Subscribe config change
    this.coreConfigService.config.pipe(takeUntil(this._unsubscribeAll)).subscribe(config => {
      this.coreConfig = config;
    });

    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });
    this.accountService.getAuthenticationState().subscribe(account => (this.account = account));

    this.isCollapsed = this.coreSidebarService.getSidebarRegistry('menu').collapsed;

    // Close the menu on router NavigationEnd (Required for small screen to close the menu on select)
    this.router.events
      .pipe(
        filter(event => event instanceof NavigationEnd),
        takeUntil(this._unsubscribeAll)
      )
      .subscribe(() => {
        this.coreSidebarService.getSidebarRegistry('menu').close();
      });

    // scroll to active on navigation end
    this.router.events
      .pipe(
        filter(event => event instanceof NavigationEnd),
        take(1)
      )
      .subscribe(() => {
        setTimeout(() => {
          this.directiveRef?.scrollToElement('.navigation .active', -180, 500);
        });
      });
  }

  ngOnDestroy(): void {
    // Unsubscribe from all subscriptions
    this._unsubscribeAll.next();
    this._unsubscribeAll.complete();
  }

  onSidebarScroll(): void {
    if (this.directiveRef) {
      this.isScrolled = this.directiveRef.position(true).y > 3;
    }
  }

  /**
   * Toggle sidebar expanded status
   */
  toggleSidebar(): void {
    this.coreSidebarService.getSidebarRegistry('menu').toggleOpen();
  }

  /**
   * Toggle sidebar collapsed status
   */
  toggleSidebarCollapsible(): void {
    // Get the current menu state
    this.coreConfigService
      .getConfig()
      .pipe(takeUntil(this._unsubscribeAll))
      .subscribe(config => {
        this.isCollapsed = config.layout.menu.collapsed;
      });

    if (this.isCollapsed) {
      this.coreConfigService.setConfig({ layout: { menu: { collapsed: false } } }, { emitEvent: true });
    } else {
      this.coreConfigService.setConfig({ layout: { menu: { collapsed: true } } }, { emitEvent: true });
    }
  }

  collapseMenus(): void {
    this.adminMenuCollapsed = true;
  }
}
