import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Company, ICompany } from '../../model/company.model';
import { FormBuilder, Validators } from '@angular/forms';
import { CompanyService } from '../../service/company.service';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { finalize } from 'rxjs/operators';
import { AccountService } from '../../../../core/auth/account.service';

@Component({
  selector: 'stl-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss'],
})
export class OverviewComponent implements OnInit {
  public editForm = this.formBuilder.group({
    name: [null, [Validators.required]],
    commercialName: [],
    nif: [null, [Validators.required]],
    logo: [],
    vat: [],
    url: [null, [Validators.required]],
    phone: [null, [Validators.required]],
    email: [null, [Validators.required]],
    token: [null, [Validators.required]],
    type: [],
    industry: [],
    companySize: [],
  });

  public isSaving = false;

  private companyReference?: string;

  constructor(
    private activatedRoute: ActivatedRoute,
    private formBuilder: FormBuilder,
    private companyService: CompanyService,
    private accountService: AccountService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ company }) => {
      this.updateForm(company);
      if (this.accountService.isAdmin()) {
        this.companyReference = this.activatedRoute.snapshot.parent?.params.reference;
      } else {
        this.companyReference = company.reference;
      }
    });
  }

  save(): void {
    const company = this.createFromForm();
    this.subscribeToSaveResponse(this.companyService.update(company));
  }

  protected updateForm(company: ICompany): void {
    this.editForm.patchValue({
      name: company.name,
      commercialName: company.commercialName,
      nif: company.nif,
      logo: company.logo,
      vat: company.vat,
      url: company.url,
      phone: company.phone,
      email: company.email,
      type: company.type,
      industry: company.industry,
      companySize: company.companySize,
    });
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompany>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    console.error('success');
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected createFromForm(): ICompany {
    return {
      ...new Company(),
      name: this.editForm.get(['name'])!.value,
      commercialName: this.editForm.get(['commercialName'])!.value,
      nif: this.editForm.get(['nif'])!.value,
      logo: this.editForm.get(['logo'])!.value,
      vat: this.editForm.get(['vat'])!.value,
      url: this.editForm.get(['url'])!.value,
      reference: this.companyReference,
      phone: this.editForm.get(['phone'])!.value,
      email: this.editForm.get(['email'])!.value,
      type: this.editForm.get(['type'])!.value,
      industry: this.editForm.get(['industry'])!.value,
      companySize: this.editForm.get(['companySize'])!.value,
    };
  }
}
