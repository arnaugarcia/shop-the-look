jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISpace, Space } from '../space.model';
import { SpaceService } from '../service/space.service';

import { SpaceRoutingResolveService } from './space-routing-resolve.service';

describe('Service Tests', () => {
  describe('Space routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SpaceRoutingResolveService;
    let service: SpaceService;
    let resultSpace: ISpace | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SpaceRoutingResolveService);
      service = TestBed.inject(SpaceService);
      resultSpace = undefined;
    });

    describe('resolve', () => {
      it('should return ISpace returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSpace = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSpace).toEqual({ id: 123 });
      });

      it('should return new ISpace if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSpace = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSpace).toEqual(new Space());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Space })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSpace = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSpace).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
