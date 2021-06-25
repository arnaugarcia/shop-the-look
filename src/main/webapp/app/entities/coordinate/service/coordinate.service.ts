import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICoordinate, getCoordinateIdentifier } from '../coordinate.model';

export type EntityResponseType = HttpResponse<ICoordinate>;
export type EntityArrayResponseType = HttpResponse<ICoordinate[]>;

@Injectable({ providedIn: 'root' })
export class CoordinateService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/coordinates');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(coordinate: ICoordinate): Observable<EntityResponseType> {
    return this.http.post<ICoordinate>(this.resourceUrl, coordinate, { observe: 'response' });
  }

  update(coordinate: ICoordinate): Observable<EntityResponseType> {
    return this.http.put<ICoordinate>(`${this.resourceUrl}/${getCoordinateIdentifier(coordinate) as number}`, coordinate, {
      observe: 'response',
    });
  }

  partialUpdate(coordinate: ICoordinate): Observable<EntityResponseType> {
    return this.http.patch<ICoordinate>(`${this.resourceUrl}/${getCoordinateIdentifier(coordinate) as number}`, coordinate, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICoordinate>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICoordinate[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCoordinateToCollectionIfMissing(
    coordinateCollection: ICoordinate[],
    ...coordinatesToCheck: (ICoordinate | null | undefined)[]
  ): ICoordinate[] {
    const coordinates: ICoordinate[] = coordinatesToCheck.filter(isPresent);
    if (coordinates.length > 0) {
      const coordinateCollectionIdentifiers = coordinateCollection.map(coordinateItem => getCoordinateIdentifier(coordinateItem)!);
      const coordinatesToAdd = coordinates.filter(coordinateItem => {
        const coordinateIdentifier = getCoordinateIdentifier(coordinateItem);
        if (coordinateIdentifier == null || coordinateCollectionIdentifiers.includes(coordinateIdentifier)) {
          return false;
        }
        coordinateCollectionIdentifiers.push(coordinateIdentifier);
        return true;
      });
      return [...coordinatesToAdd, ...coordinateCollection];
    }
    return coordinateCollection;
  }
}
