import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISpace, SpaceRequest } from '../space.model';

export type EntityResponseType = HttpResponse<ISpace>;
export type EntityArrayResponseType = HttpResponse<ISpace[]>;

@Injectable({ providedIn: 'root' })
export class SpaceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/spaces');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(request: SpaceRequest): Observable<EntityResponseType> {
    return this.http.post<ISpace>(this.resourceUrl, request, { observe: 'response' });
  }

  update(space: ISpace): Observable<EntityResponseType> {
    return this.http.put<ISpace>(`${this.resourceUrl}/${space.reference!}`, space, { observe: 'response' });
  }

  partialUpdate(space: ISpace): Observable<EntityResponseType> {
    return this.http.patch<ISpace>(`${this.resourceUrl}/${space.reference!}`, space, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISpace>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISpace[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(reference: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${reference}`, { observe: 'response' });
  }
}
