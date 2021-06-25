jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISpaceTemplate, SpaceTemplate } from '../space-template.model';
import { SpaceTemplateService } from '../service/space-template.service';

import { SpaceTemplateRoutingResolveService } from './space-template-routing-resolve.service';

describe('Service Tests', () => {
  describe('SpaceTemplate routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SpaceTemplateRoutingResolveService;
    let service: SpaceTemplateService;
    let resultSpaceTemplate: ISpaceTemplate | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SpaceTemplateRoutingResolveService);
      service = TestBed.inject(SpaceTemplateService);
      resultSpaceTemplate = undefined;
    });

    describe('resolve', () => {
      it('should return ISpaceTemplate returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSpaceTemplate = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSpaceTemplate).toEqual({ id: 123 });
      });

      it('should return new ISpaceTemplate if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSpaceTemplate = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSpaceTemplate).toEqual(new SpaceTemplate());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SpaceTemplate })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSpaceTemplate = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSpaceTemplate).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
