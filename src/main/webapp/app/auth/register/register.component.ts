import { AfterViewInit, Component, ElementRef, ViewChild } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';

import { EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE, NIF_ALREADY_USED_TYPE } from 'app/config/error.constants';
import { RegisterService } from './register.service';
import { CoreConfigService } from '@core/services/config.service';
import { CoreConfig } from '../../../@core/types';

const PATTERN_URL = "^(?:http(s)?:\\/\\/)?[\\w.-]+(?:\\.[\\w\\.-]+)+[\\w\\-\\._~:/?#[\\]@!\\$&'\\(\\)\\*\\+,;=.]+$";

const PATTERN_LOGIN = '^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$';

const PATTERN_NIF = '([a-z]|[A-Z]|[0-9])[0-9]{7}([a-z]|[A-Z]|[0-9])';

@Component({
  selector: 'stl-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent implements AfterViewInit {
  @ViewChild('companyName', { static: false })
  companyName?: ElementRef;

  doNotMatch = false;
  error = false;
  errorEmailExists = false;
  errorUserExists = false;
  errorNIFExists = false;
  success = false;

  registerForm = this.fb.group({
    login: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(50), Validators.pattern(PATTERN_LOGIN)]],
    email: ['', [Validators.required, Validators.email]],
    name: ['', Validators.required],
    phone: ['', Validators.required],
    url: ['', [Validators.required, Validators.pattern(PATTERN_URL)]],
    nif: ['', [Validators.required, Validators.pattern(PATTERN_NIF)]],
    password: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
    confirmPassword: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
  });
  coreConfig: CoreConfig = new CoreConfig();
  passwordTextType = true;

  constructor(
    private coreConfigService: CoreConfigService,
    private translateService: TranslateService,
    private registerService: RegisterService,
    private fb: FormBuilder
  ) {
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

  ngAfterViewInit(): void {
    if (this.companyName) {
      this.companyName.nativeElement.focus();
    }
  }

  register(): void {
    this.doNotMatch = false;
    this.error = false;
    this.errorEmailExists = false;
    this.errorUserExists = false;

    const password = this.registerForm.get(['password'])!.value;
    if (password !== this.registerForm.get(['confirmPassword'])!.value) {
      this.doNotMatch = true;
    } else {
      const name = this.registerForm.get(['name'])!.value;
      const nif = this.registerForm.get(['nif'])!.value;
      const url = this.registerForm.get(['url'])!.value;
      const phone = this.registerForm.get(['phone'])!.value;
      const login = this.registerForm.get(['login'])!.value;
      const email = this.registerForm.get(['email'])!.value;
      this.registerService
        .save({
          name,
          nif,
          phone,
          url,
          login,
          email,
          password,
          langKey: this.translateService.currentLang,
        })
        .subscribe(
          () => (this.success = true),
          response => this.processError(response)
        );
    }
  }

  /**
   * Toggle password
   */
  togglePasswordTextType(): void {
    this.passwordTextType = !this.passwordTextType;
  }

  private processError(response: HttpErrorResponse): void {
    if (response.status === 400 && response.error.type === LOGIN_ALREADY_USED_TYPE) {
      this.errorUserExists = true;
    } else if (response.status === 400 && response.error.type === EMAIL_ALREADY_USED_TYPE) {
      this.errorEmailExists = true;
    } else if (response.status === 400 && response.error.type === NIF_ALREADY_USED_TYPE) {
      this.errorNIFExists = true;
    } else {
      this.error = true;
    }
  }
}
