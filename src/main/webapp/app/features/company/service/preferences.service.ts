import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IPreferences, PreferencesRequest } from '../model/preferences.model';

export type EntityResponseType = HttpResponse<IPreferences>;

@Injectable({ providedIn: 'root' })
export class PreferencesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/preferences');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  update(preferenceRequest: PreferencesRequest): Observable<EntityResponseType> {
    return this.http.put<IPreferences>(this.resourceUrl, preferenceRequest, {
      observe: 'response',
    });
  }

  query(): Observable<EntityResponseType> {
    return this.http.get<IPreferences>(this.resourceUrl, { observe: 'response' });
  }

  find(companyReference: string): Observable<EntityResponseType> {
    return this.http.get<IPreferences>(`${this.resourceUrl}/${companyReference}`, { observe: 'response' });
  }

  updateFor(preferenceRequest: PreferencesRequest, companyReference: string): Observable<EntityResponseType> {
    return this.http.put<IPreferences>(`${this.resourceUrl}/${companyReference}`, preferenceRequest, { observe: 'response' });
  }
}
