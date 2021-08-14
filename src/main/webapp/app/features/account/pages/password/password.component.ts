import { Component } from '@angular/core';

@Component({
  selector: 'stl-password',
  templateUrl: './password.component.html',
  styleUrls: ['./password.component.scss'],
})
export class PasswordComponent {
  public passwordTextTypeOld = false;
  public passwordTextTypeNew = false;
  public passwordTextTypeRetype = false;

  /**
   * Toggle Password Text Type Old
   */
  togglePasswordTextTypeOld(): void {
    this.passwordTextTypeOld = !this.passwordTextTypeOld;
  }

  /**
   * Toggle Password Text Type New
   */
  togglePasswordTextTypeNew(): void {
    this.passwordTextTypeNew = !this.passwordTextTypeNew;
  }

  /**
   * Toggle Password Text Type Retype
   */
  togglePasswordTextTypeRetype(): void {
    this.passwordTextTypeRetype = !this.passwordTextTypeRetype;
  }
}
