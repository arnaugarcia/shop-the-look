import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CoreConfigService } from '@core/services/config.service';

import { AccountService } from 'app/core/auth/account.service';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { LoginService } from './login.service';
import { CoreConfig } from '../../../@core/types';

@Component({
  selector: 'stl-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit, AfterViewInit {
  public coreConfig: CoreConfig = new CoreConfig();
  public submitted = false;
  public passwordTextType = false;

  @ViewChild('username', { static: false })
  username!: ElementRef;

  authenticationError = false;

  loginForm = this.fb.group({
    username: [null, [Validators.required]],
    password: [null, [Validators.required]],
    rememberMe: [false],
  });

  private _unsubscribeAll: Subject<any>;

  constructor(
    private coreConfigService: CoreConfigService,
    private accountService: AccountService,
    private loginService: LoginService,
    private router: Router,
    private fb: FormBuilder
  ) {
    this._unsubscribeAll = new Subject();

    // Configure the layout
    this.coreConfigService.setConfig({
      layout: {
        navbar: {
          hidden: true,
        },
        menu: {
          hidden: true,
        },
        footer: {
          hidden: true,
        },
        customizer: false,
        enableLocalStorage: false,
      },
    });
  }

  ngOnInit(): void {
    // if already authenticated then navigate to home page
    this.accountService.identity().subscribe(() => {
      if (this.accountService.isAuthenticated()) {
        this.router.navigate(['']);
      }
    });

    // Subscribe to config changes
    this.coreConfigService.config.pipe(takeUntil(this._unsubscribeAll)).subscribe((config: CoreConfig) => {
      this.coreConfig = config;
    });
  }

  ngAfterViewInit(): void {
    this.username.nativeElement.focus();
  }

  login(): void {
    this.loginService
      .login({
        username: this.loginForm.get('username')!.value,
        password: this.loginForm.get('password')!.value,
        rememberMe: this.loginForm.get('rememberMe')!.value,
      })
      .subscribe(
        () => {
          this.authenticationError = false;
          if (!this.router.getCurrentNavigation()) {
            // There were no routing during login (eg from navigationToStoredUrl)
            this.router.navigate(['']);
          }
        },
        () => (this.authenticationError = true)
      );
  }

  get form(): any {
    return this.loginForm.controls;
  }

  /**
   * Toggle password
   */
  togglePasswordTextType(): void {
    this.passwordTextType = !this.passwordTextType;
  }
}
