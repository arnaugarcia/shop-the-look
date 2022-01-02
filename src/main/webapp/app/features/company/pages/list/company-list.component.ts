import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { CompanyService } from 'app/features/company/service/company.service';
import { ICompany } from '../../model/company.model';

@Component({
  selector: 'stl-company-list',
  templateUrl: './company-list.component.html',
})
export class CompanyListComponent implements OnInit {
  companies?: ICompany[];
  isLoading = false;

  constructor(protected companyService: CompanyService) {}

  loadAll(): void {
    this.isLoading = true;

    this.companyService.query().subscribe(
      (res: HttpResponse<ICompany[]>) => {
        this.isLoading = false;
        this.companies = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }
}
