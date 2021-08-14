import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'stl-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss'],
})
export class AccountComponent implements OnInit {
  public contentHeader: any;

  public passwordTextTypeOld = false;
  public passwordTextTypeNew = false;
  public passwordTextTypeRetype = false;

  ngOnInit(): void {
    this.contentHeader = {
      headerTitle: 'Account Settings',
      actionButton: true,
      breadcrumb: {
        type: '',
        links: [
          {
            name: 'Home',
            isLink: true,
            link: '/',
          },
          {
            name: 'Account',
            isLink: true,
            link: 'settings',
          },
          {
            name: 'Account Settings',
            isLink: false,
          },
        ],
      },
    };
  }

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
