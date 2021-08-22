import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IPreferences } from '../pages/preferences/preferences.model';

export type EntityResponseType = HttpResponse<IPreferences>;

@Injectable({ providedIn: 'root' })
export class PreferencesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/preferences');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  update(preferenceRequest: IPreferences): Observable<EntityResponseType> {
    return this.http.put<IPreferences>(`${this.resourceUrl}`, preferenceRequest, {
      observe: 'response',
    });
  }

  query(): Observable<EntityResponseType> {
    return this.http.get<IPreferences>(this.resourceUrl, { observe: 'response' });
  }
}
