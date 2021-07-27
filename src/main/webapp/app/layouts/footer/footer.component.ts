import { Component, OnDestroy, OnInit, ViewEncapsulation } from '@angular/core';
import { CoreConfig } from '../../../@core/types';
import { Subject } from 'rxjs';
import { CoreConfigService } from '../../../@core/services/config.service';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'footer', // eslint-disable-line @angular-eslint/component-selector
  templateUrl: './footer.component.html',
  encapsulation: ViewEncapsulation.None,
})
export class FooterComponent implements OnInit, OnDestroy {
  public coreConfig: CoreConfig | undefined;
  public year: number = new Date().getFullYear();

  // Private
  private _unsubscribeAll: Subject<any>;

  /**
   * Constructor
   *
   * @param {CoreConfigService} _coreConfigService
   */
  constructor(public _coreConfigService: CoreConfigService) {
    // Set the private defaults
    this._unsubscribeAll = new Subject();
  }

  // Lifecycle hooks
  // -----------------------------------------------------------------------------------------------------

  /**
   * On init
   */
  ngOnInit(): void {
    // Subscribe to config changes
    this._coreConfigService.config?.pipe(takeUntil(this._unsubscribeAll)).subscribe((config: CoreConfig) => {
      this.coreConfig = config;
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
}
