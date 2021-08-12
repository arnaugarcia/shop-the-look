import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { Observable } from 'rxjs';
import { IPreferences } from './preferences.model';

export type EntityResponseType = HttpResponse<IPreferences>;

@Injectable({ providedIn: 'root' })
export class PreferenceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/preferences/');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  update(companyReference: string, preferences: IPreferences): Observable<EntityResponseType> {
    return this.http.put<IPreferences>(`${this.resourceUrl}/${companyReference!}`, preferences, { observe: 'response' });
  }

  find(companyReference: string): Observable<EntityResponseType> {
    return this.http.get<IPreferences>(`${this.resourceUrl}/${companyReference}`, { observe: 'response' });
  }
}
