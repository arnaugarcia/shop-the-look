import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import { CoreSidebarService } from '../../../../../@core/components/core-sidebar/core-sidebar.service';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { EmployeeService } from '../../services/employee.service';
import { ITEMS_PER_PAGE } from '../../../../config/pagination.constants';
import { AccountStatus, IEmployee } from 'app/features/employee/models/employee.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { EmployeeDeleteDialogComponent } from '../../components/employee-delete/employee-delete-dialog.component';
import { Subject, Subscription } from 'rxjs';
import { CoreSidebarComponent } from '../../../../../@core/components/core-sidebar/core-sidebar.component';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { ICompany } from '../../../company/model/company.model';
import { CompanyService } from '../../../company/service/company.service';
import { ContentHeader } from '../../../../layouts/content-header/content-header.component';

@Component({
  selector: 'stl-employee-list',
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.scss'],
})
export class EmployeeListComponent implements OnInit, OnDestroy, AfterViewInit {
  readonly NEW_USER_SIDEBAR_KEY = 'new-user-sidebar';
  public selectedOption = 10;
  public employees: IEmployee[] = [];
  public page?: number;
  public itemsPerPage = ITEMS_PER_PAGE;
  public totalItems = 0;
  public ngbPaginationPage = 1;
  public isLoading = false;
  public companies: ICompany[] = [];
  public AccountStatus = AccountStatus;
  public sidebar!: CoreSidebarComponent;
  public contentHeader: ContentHeader;

  public searchText = '';
  public searchTextChanged: Subject<string> = new Subject<string>();
  public companySearch = '';
  public companySelected: Subject<string> = new Subject<string>();

  private subscriptions = new Subscription();

  constructor(
    private coreSidebarService: CoreSidebarService,
    private employeeService: EmployeeService,
    private companyService: CompanyService,
    private modalService: NgbModal
  ) {
    this.contentHeader = {
      headerTitle: 'Employees',
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
            name: 'Employees',
            isLink: false,
          },
        ],
      },
    };
  }

  ngOnInit(): void {
    this.loadPage();
    this.companyService.query().subscribe((response: HttpResponse<ICompany[]>) => {
      if (response.body) {
        this.companies = response.body;
      }
    });
    this.subscriptions.add(
      this.searchTextChanged.pipe(debounceTime(500), distinctUntilChanged()).subscribe((newText: string) => {
        this.searchText = newText;
        this.loadPage();
      })
    );
    this.subscriptions.add(
      this.companySelected.subscribe((companyReference: string) => {
        this.companySearch = companyReference;
        this.loadPage();
      })
    );
  }

  ngAfterViewInit(): void {
    this.sidebar = this.coreSidebarService.getSidebarRegistry(this.NEW_USER_SIDEBAR_KEY);
    this.subscriptions.add(
      this.sidebar.statusChangedEvent.asObservable().subscribe((status: string) => {
        if (status === 'success') {
          this.loadPage();
        }
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  loadPage(page?: number): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.employeeService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        keyword: this.searchText,
        company: this.companySearch,
      })
      .subscribe(
        (res: HttpResponse<IEmployee[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  public isAdmin(authorities: string[]): boolean {
    return authorities.includes('ROLE_ADMIN');
  }

  public isManager(authorities: string[]): boolean {
    return authorities.includes('ROLE_MANAGER') && !authorities.includes('ROLE_ADMIN');
  }

  public isUser(authorities: string[]): boolean {
    return authorities.includes('ROLE_USER') && !authorities.includes('ROLE_MANAGER') && !authorities.includes('ROLE_ADMIN');
  }

  toggleManager(employee: IEmployee): void {
    this.employeeService.manager(employee).subscribe(
      () => this.loadPage(),
      (error: HttpErrorResponse) => {
        console.error(error);
      }
    );
  }

  removeEmployee(employee: IEmployee): void {
    const modalRef = this.modalService.open(EmployeeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.employee = employee;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  private onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  private onSuccess(data: IEmployee[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.employees = data ?? [];
    this.ngbPaginationPage = this.page;
  }
}
