import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'stl-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss'],
})
export class AccountComponent implements OnInit {
  public contentHeader: any;

  ngOnInit(): void {
    this.contentHeader = {
      headerTitle: 'Account Settings',
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
}
