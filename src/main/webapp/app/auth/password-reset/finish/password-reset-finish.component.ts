import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { PasswordResetFinishService } from './password-reset-finish.service';
import { CoreConfig } from '../../../../@core/types';
import { CoreConfigService } from '../../../../@core/services/config.service';

@Component({
  selector: 'stl-password-reset-finish',
  templateUrl: './password-reset-finish.component.html',
  styleUrls: ['./password-reset-finish.component.scss'],
})
export class PasswordResetFinishComponent implements OnInit, AfterViewInit {
  @ViewChild('newPassword', { static: false })
  newPassword?: ElementRef;

  public coreConfig!: CoreConfig;

  public submitted = false;

  initialized = false;
  doNotMatch = false;
  error = false;
  success = false;
  key = '';

  passwordForm = this.fb.group({
    newPassword: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
    confirmPassword: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
  });
  passwordTextType = false;

  constructor(
    private coreConfigService: CoreConfigService,
    private passwordResetFinishService: PasswordResetFinishService,
    private route: ActivatedRoute,
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

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      if (params['key']) {
        this.key = params['key'];
      }
      this.initialized = true;
    });
  }

  ngAfterViewInit(): void {
    if (this.newPassword) {
      this.newPassword.nativeElement.focus();
    }
  }

  get form(): any {
    return this.passwordForm.controls;
  }

  /**
   * Toggle password
   */
  togglePasswordTextType(): void {
    this.passwordTextType = !this.passwordTextType;
  }

  finishReset(): void {
    this.submitted = true;
    this.doNotMatch = false;
    this.error = false;

    const newPassword = this.passwordForm.get(['newPassword'])!.value;
    const confirmPassword = this.passwordForm.get(['confirmPassword'])!.value;

    if (newPassword !== confirmPassword) {
      this.doNotMatch = true;
    } else {
      this.passwordResetFinishService.save(this.key, newPassword).subscribe(
        () => (this.success = true),
        () => (this.error = true)
      );
    }
  }
}
