import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISpace, getSpaceIdentifier } from '../space.model';

export type EntityResponseType = HttpResponse<ISpace>;
export type EntityArrayResponseType = HttpResponse<ISpace[]>;

@Injectable({ providedIn: 'root' })
export class SpaceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/spaces');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(space: ISpace): Observable<EntityResponseType> {
    return this.http.post<ISpace>(this.resourceUrl, space, { observe: 'response' });
  }

  update(space: ISpace): Observable<EntityResponseType> {
    return this.http.put<ISpace>(`${this.resourceUrl}/${getSpaceIdentifier(space) as number}`, space, { observe: 'response' });
  }

  partialUpdate(space: ISpace): Observable<EntityResponseType> {
    return this.http.patch<ISpace>(`${this.resourceUrl}/${getSpaceIdentifier(space) as number}`, space, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISpace>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISpace[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSpaceToCollectionIfMissing(spaceCollection: ISpace[], ...spacesToCheck: (ISpace | null | undefined)[]): ISpace[] {
    const spaces: ISpace[] = spacesToCheck.filter(isPresent);
    if (spaces.length > 0) {
      const spaceCollectionIdentifiers = spaceCollection.map(spaceItem => getSpaceIdentifier(spaceItem)!);
      const spacesToAdd = spaces.filter(spaceItem => {
        const spaceIdentifier = getSpaceIdentifier(spaceItem);
        if (spaceIdentifier == null || spaceCollectionIdentifiers.includes(spaceIdentifier)) {
          return false;
        }
        spaceCollectionIdentifiers.push(spaceIdentifier);
        return true;
      });
      return [...spacesToAdd, ...spaceCollection];
    }
    return spaceCollection;
  }
}
