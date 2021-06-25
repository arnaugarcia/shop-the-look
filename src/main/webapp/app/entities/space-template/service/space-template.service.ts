import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISpaceTemplate, getSpaceTemplateIdentifier } from '../space-template.model';

export type EntityResponseType = HttpResponse<ISpaceTemplate>;
export type EntityArrayResponseType = HttpResponse<ISpaceTemplate[]>;

@Injectable({ providedIn: 'root' })
export class SpaceTemplateService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/space-templates');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(spaceTemplate: ISpaceTemplate): Observable<EntityResponseType> {
    return this.http.post<ISpaceTemplate>(this.resourceUrl, spaceTemplate, { observe: 'response' });
  }

  update(spaceTemplate: ISpaceTemplate): Observable<EntityResponseType> {
    return this.http.put<ISpaceTemplate>(`${this.resourceUrl}/${getSpaceTemplateIdentifier(spaceTemplate) as number}`, spaceTemplate, {
      observe: 'response',
    });
  }

  partialUpdate(spaceTemplate: ISpaceTemplate): Observable<EntityResponseType> {
    return this.http.patch<ISpaceTemplate>(`${this.resourceUrl}/${getSpaceTemplateIdentifier(spaceTemplate) as number}`, spaceTemplate, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISpaceTemplate>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISpaceTemplate[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSpaceTemplateToCollectionIfMissing(
    spaceTemplateCollection: ISpaceTemplate[],
    ...spaceTemplatesToCheck: (ISpaceTemplate | null | undefined)[]
  ): ISpaceTemplate[] {
    const spaceTemplates: ISpaceTemplate[] = spaceTemplatesToCheck.filter(isPresent);
    if (spaceTemplates.length > 0) {
      const spaceTemplateCollectionIdentifiers = spaceTemplateCollection.map(
        spaceTemplateItem => getSpaceTemplateIdentifier(spaceTemplateItem)!
      );
      const spaceTemplatesToAdd = spaceTemplates.filter(spaceTemplateItem => {
        const spaceTemplateIdentifier = getSpaceTemplateIdentifier(spaceTemplateItem);
        if (spaceTemplateIdentifier == null || spaceTemplateCollectionIdentifiers.includes(spaceTemplateIdentifier)) {
          return false;
        }
        spaceTemplateCollectionIdentifiers.push(spaceTemplateIdentifier);
        return true;
      });
      return [...spaceTemplatesToAdd, ...spaceTemplateCollection];
    }
    return spaceTemplateCollection;
  }
}
