import { Component, OnInit } from '@angular/core';
import { PreferencesService } from '../../service/preferences.service';
import { FormBuilder, Validators } from '@angular/forms';
import { IPreferences } from '../../model/preferences.model';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'stl-preferences',
  templateUrl: './preferences.component.html',
  styleUrls: ['./preferences.component.scss'],
})
export class PreferencesComponent implements OnInit {
  public preferences?: IPreferences;
  public preferencesForm;

  constructor(private activatedRoute: ActivatedRoute, private preferencesService: PreferencesService, private formBuilder: FormBuilder) {
    this.preferencesForm = this.formBuilder.group({
      url: ['', [Validators.required]],
      importMethod: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ preferences }) => {
      this.updateForm(preferences);
      this.preferences = preferences;
    });
  }

  save(): void {
    console.error('asdfasfd');
  }

  private updateForm(preferences: IPreferences): void {
    this.preferencesForm.patchValue({
      url: preferences.feedUrl,
      importMethod: preferences.importMethod,
    });
  }
}
