import { Component, OnInit } from '@angular/core';
import { PreferencesService } from '../../service/preferences.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { IPreferences, PreferencesRequest } from '../../model/preferences.model';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CompanyModalSuccessComponent } from '../../component/company-modal-success/company-modal-success.component';
import { CompanyModalErrorComponent } from '../../component/company-modal-error/company-modal-error.component';

@Component({
  selector: 'stl-preferences',
  templateUrl: './preferences.component.html',
  styleUrls: ['./preferences.component.scss'],
})
export class PreferencesComponent implements OnInit {
  public preferences?: IPreferences;
  public preferencesForm: FormGroup;
  private readonly companyReference?: string;

  constructor(
    private activatedRoute: ActivatedRoute,
    private preferencesService: PreferencesService,
    private formBuilder: FormBuilder,
    private modalService: NgbModal
  ) {
    this.preferencesForm = this.formBuilder.group({
      url: ['', [Validators.required]],
      importMethod: ['', [Validators.required]],
    });
    this.companyReference = this.activatedRoute.parent?.snapshot.params.reference;
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ preferences }) => {
      this.updateForm(preferences);
      this.preferences = preferences;
    });
  }

  save(): void {
    const preferences = this.createFromForm();
    if (this.companyReference) {
      this.subscribeToSaveResponse(this.preferencesService.updateFor(preferences, this.companyReference));
    } else {
      this.subscribeToSaveResponse(this.preferencesService.update(preferences));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPreferences>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected createFromForm(): IPreferences {
    return {
      ...new PreferencesRequest(),
      feedUrl: this.preferencesForm.get(['url'])!.value,
      importMethod: this.preferencesForm.get(['importMethod'])!.value,
    };
  }

  private updateForm(preferences: IPreferences): void {
    this.preferencesForm.patchValue({
      url: preferences.feedUrl,
      importMethod: preferences.importMethod,
    });
  }

  private onSaveSuccess(): void {
    this.modalService.open(CompanyModalSuccessComponent);
  }

  private onSaveError(): void {
    this.modalService.open(CompanyModalErrorComponent);
  }
}
