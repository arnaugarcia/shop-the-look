import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CompanyDeleteDialogComponent } from '../../../../entities/company/delete/company-delete-dialog.component';
import { CompanyService } from 'app/features/company/service/company.service';
import { ICompany } from '../../model/company.model';

@Component({
  selector: 'stl-company-list',
  templateUrl: './company-list.component.html',
})
export class CompanyListComponent implements OnInit {
  companies?: ICompany[];
  isLoading = false;

  constructor(protected companyService: CompanyService, protected modalService: NgbModal) {}

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

  trackId(index: number, item: ICompany): number {
    return item.id!;
  }

  delete(company: ICompany): void {
    const modalRef = this.modalService.open(CompanyDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.company = company;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
