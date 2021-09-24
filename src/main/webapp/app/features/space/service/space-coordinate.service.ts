import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { CoordinateCreateRequest, ICoordinate } from '../model/coordinate.model';
import { Observable } from 'rxjs';

export type EntityResponseType = HttpResponse<ICoordinate>;

@Injectable({
  providedIn: 'root',
})
export class SpaceCoordinateService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/spaces/');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  addCoordinate(spaceReference: string, request: CoordinateCreateRequest): Observable<EntityResponseType> {
    return this.http.put<ICoordinate>(`${this.resourceUrl}/${spaceReference}/coordinates`, request, { observe: 'response' });
  }

  removeCoordinate(spaceReference: string, coordinateReference: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${spaceReference}/coordinates/${coordinateReference}`, { observe: 'response' });
  }
}
