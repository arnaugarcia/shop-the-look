import { Component, OnInit } from '@angular/core';
import { snippetCode } from '../../../../../@core/components/card-snippet/card-snippet.component';
import { ActivatedRoute } from '@angular/router';
import { ICompany } from '../../model/company.model';

@Component({
  selector: 'stl-api-key',
  templateUrl: './api-key.component.html',
  styleUrls: ['./api-key.component.scss'],
})
export class ApiKeyComponent implements OnInit {
  public error = false;
  public snippetCode?: snippetCode;
  private company?: ICompany;

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.data.subscribe(({ company }) => {
      this.company = company;
      if (this.company) {
        this.snippetCode = {
          html: `<script async defer src='https://cdn.shopthelook.com/client/shopthelook.js?v=1.0&key=${this.company.token!}'></script>`,
          isCollapsed: false,
        };
      } else {
        this.error = true;
      }
    });
  }
}
