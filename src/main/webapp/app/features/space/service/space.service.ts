import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { EMPTY, Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISpace, PhotoRequest, SpaceRequest } from '../model/space.model';

export type EntityResponseType = HttpResponse<ISpace>;
export type EntityArrayResponseType = HttpResponse<ISpace[]>;

@Injectable({ providedIn: 'root' })
export class SpaceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/spaces');
  protected resourceMeUrl = this.applicationConfigService.getEndpointFor('api/me/spaces');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(request: SpaceRequest): Observable<EntityResponseType> {
    return this.http.post<ISpace>(this.resourceMeUrl, request, { observe: 'response' });
  }

  createForCompany(request: SpaceRequest, companyReference: string): Observable<EntityResponseType> {
    const options = createRequestOption({ companyReference: companyReference });
    return this.http.post<ISpace>(this.resourceUrl, request, { observe: 'response', params: options });
  }

  addPhoto(spaceReference: string, photoRequest: PhotoRequest): Observable<never> {
    console.error(photoRequest);
    return EMPTY;
  }

  removePhoto(photoReference: string): Observable<never> {
    return EMPTY;
  }

  update(space: ISpace): Observable<EntityResponseType> {
    return this.http.put<ISpace>(`${this.resourceUrl}/${space.reference!}`, space, { observe: 'response' });
  }

  find(reference: string): Observable<EntityResponseType> {
    return this.http.get<ISpace>(`${this.resourceUrl}/${reference}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISpace[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(reference: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${reference}`, { observe: 'response' });
  }
}
