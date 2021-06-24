import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { SubscriptionCategory } from 'app/entities/enumerations/subscription-category.model';
import { ISubscriptionPlan, SubscriptionPlan } from '../subscription-plan.model';

import { SubscriptionPlanService } from './subscription-plan.service';

describe('Service Tests', () => {
  describe('SubscriptionPlan Service', () => {
    let service: SubscriptionPlanService;
    let httpMock: HttpTestingController;
    let elemDefault: ISubscriptionPlan;
    let expectedResult: ISubscriptionPlan | ISubscriptionPlan[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SubscriptionPlanService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        description: 'AAAAAAA',
        category: SubscriptionCategory.BRONCE,
        maxProducts: 0,
        maxSpaces: 0,
        maxRequests: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a SubscriptionPlan', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new SubscriptionPlan()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SubscriptionPlan', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
            category: 'BBBBBB',
            maxProducts: 1,
            maxSpaces: 1,
            maxRequests: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a SubscriptionPlan', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
            description: 'BBBBBB',
            maxRequests: 1,
          },
          new SubscriptionPlan()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of SubscriptionPlan', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
            category: 'BBBBBB',
            maxProducts: 1,
            maxSpaces: 1,
            maxRequests: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a SubscriptionPlan', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSubscriptionPlanToCollectionIfMissing', () => {
        it('should add a SubscriptionPlan to an empty array', () => {
          const subscriptionPlan: ISubscriptionPlan = { id: 123 };
          expectedResult = service.addSubscriptionPlanToCollectionIfMissing([], subscriptionPlan);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(subscriptionPlan);
        });

        it('should not add a SubscriptionPlan to an array that contains it', () => {
          const subscriptionPlan: ISubscriptionPlan = { id: 123 };
          const subscriptionPlanCollection: ISubscriptionPlan[] = [
            {
              ...subscriptionPlan,
            },
            { id: 456 },
          ];
          expectedResult = service.addSubscriptionPlanToCollectionIfMissing(subscriptionPlanCollection, subscriptionPlan);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a SubscriptionPlan to an array that doesn't contain it", () => {
          const subscriptionPlan: ISubscriptionPlan = { id: 123 };
          const subscriptionPlanCollection: ISubscriptionPlan[] = [{ id: 456 }];
          expectedResult = service.addSubscriptionPlanToCollectionIfMissing(subscriptionPlanCollection, subscriptionPlan);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(subscriptionPlan);
        });

        it('should add only unique SubscriptionPlan to an array', () => {
          const subscriptionPlanArray: ISubscriptionPlan[] = [{ id: 123 }, { id: 456 }, { id: 37588 }];
          const subscriptionPlanCollection: ISubscriptionPlan[] = [{ id: 123 }];
          expectedResult = service.addSubscriptionPlanToCollectionIfMissing(subscriptionPlanCollection, ...subscriptionPlanArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const subscriptionPlan: ISubscriptionPlan = { id: 123 };
          const subscriptionPlan2: ISubscriptionPlan = { id: 456 };
          expectedResult = service.addSubscriptionPlanToCollectionIfMissing([], subscriptionPlan, subscriptionPlan2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(subscriptionPlan);
          expect(expectedResult).toContain(subscriptionPlan2);
        });

        it('should accept null and undefined values', () => {
          const subscriptionPlan: ISubscriptionPlan = { id: 123 };
          expectedResult = service.addSubscriptionPlanToCollectionIfMissing([], null, subscriptionPlan, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(subscriptionPlan);
        });

        it('should return initial array if no SubscriptionPlan is added', () => {
          const subscriptionPlanCollection: ISubscriptionPlan[] = [{ id: 123 }];
          expectedResult = service.addSubscriptionPlanToCollectionIfMissing(subscriptionPlanCollection, undefined, null);
          expect(expectedResult).toEqual(subscriptionPlanCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
