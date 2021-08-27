import { Component, OnInit } from '@angular/core';
import { ContentHeader } from '../../../../layouts/content-header/content-header.component';
import { NavigationEnd, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'stl-company',
  templateUrl: './company.component.html',
  styleUrls: ['./company.component.scss'],
})
export class CompanyComponent implements OnInit {
  public contentHeader: ContentHeader;
  private routeSubscription = new Subscription();

  private readonly HOME_PATH = {
    name: 'Home',
    isLink: true,
    link: '/',
  };
  private readonly COMPANY_PATH = {
    name: 'Company',
    isLink: true,
    link: '/company',
  };
  private readonly OVERVIEW_PATH = {
    name: 'Overview',
    isLink: false,
  };

  constructor(private router: Router) {
    this.contentHeader = {
      headerTitle: 'Company settings',
      actionButton: true,
      breadcrumb: {
        type: '',
        links: [this.HOME_PATH, this.COMPANY_PATH, this.OVERVIEW_PATH],
      },
    };
  }

  ngOnInit(): void {
    this.routeSubscription = this.router.events.pipe(filter(event => event instanceof NavigationEnd)).subscribe(routeEvent => {
      const path = this.router.url.split('/')[2];
      this.contentHeader.breadcrumb!.links = [this.HOME_PATH, this.COMPANY_PATH];
      if (path) {
        this.contentHeader.breadcrumb!.links.push({
          name: path[0].toUpperCase() + path.substring(1),
          isLink: false,
        });
      }
    });
  }
}
