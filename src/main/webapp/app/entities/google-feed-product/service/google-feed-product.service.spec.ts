import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { GoogleFeedProductAvailability } from 'app/entities/enumerations/google-feed-product-availability.model';
import { GoogleFeedProductCondition } from 'app/entities/enumerations/google-feed-product-condition.model';
import { GoogleFeedAgeGroup } from 'app/entities/enumerations/google-feed-age-group.model';
import { IGoogleFeedProduct, GoogleFeedProduct } from '../google-feed-product.model';

import { GoogleFeedProductService } from './google-feed-product.service';

describe('Service Tests', () => {
  describe('GoogleFeedProduct Service', () => {
    let service: GoogleFeedProductService;
    let httpMock: HttpTestingController;
    let elemDefault: IGoogleFeedProduct;
    let expectedResult: IGoogleFeedProduct | IGoogleFeedProduct[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(GoogleFeedProductService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        sku: 'AAAAAAA',
        name: 'AAAAAAA',
        description: 'AAAAAAA',
        link: 'AAAAAAA',
        imageLink: 'AAAAAAA',
        additionalImageLink: 'AAAAAAA',
        mobileLink: 'AAAAAAA',
        availability: GoogleFeedProductAvailability.IN_STOCK,
        availabilityDate: currentDate,
        price: 'AAAAAAA',
        salePrice: 'AAAAAAA',
        brand: 'AAAAAAA',
        condition: GoogleFeedProductCondition.NEW,
        adult: false,
        ageGroup: GoogleFeedAgeGroup.NEWBORN,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            availabilityDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a GoogleFeedProduct', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            availabilityDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            availabilityDate: currentDate,
          },
          returnedFromService
        );

        service.create(new GoogleFeedProduct()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a GoogleFeedProduct', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            sku: 'BBBBBB',
            name: 'BBBBBB',
            description: 'BBBBBB',
            link: 'BBBBBB',
            imageLink: 'BBBBBB',
            additionalImageLink: 'BBBBBB',
            mobileLink: 'BBBBBB',
            availability: 'BBBBBB',
            availabilityDate: currentDate.format(DATE_TIME_FORMAT),
            price: 'BBBBBB',
            salePrice: 'BBBBBB',
            brand: 'BBBBBB',
            condition: 'BBBBBB',
            adult: true,
            ageGroup: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            availabilityDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a GoogleFeedProduct', () => {
        const patchObject = Object.assign(
          {
            sku: 'BBBBBB',
            name: 'BBBBBB',
            link: 'BBBBBB',
            imageLink: 'BBBBBB',
            price: 'BBBBBB',
          },
          new GoogleFeedProduct()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            availabilityDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of GoogleFeedProduct', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            sku: 'BBBBBB',
            name: 'BBBBBB',
            description: 'BBBBBB',
            link: 'BBBBBB',
            imageLink: 'BBBBBB',
            additionalImageLink: 'BBBBBB',
            mobileLink: 'BBBBBB',
            availability: 'BBBBBB',
            availabilityDate: currentDate.format(DATE_TIME_FORMAT),
            price: 'BBBBBB',
            salePrice: 'BBBBBB',
            brand: 'BBBBBB',
            condition: 'BBBBBB',
            adult: true,
            ageGroup: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            availabilityDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a GoogleFeedProduct', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addGoogleFeedProductToCollectionIfMissing', () => {
        it('should add a GoogleFeedProduct to an empty array', () => {
          const googleFeedProduct: IGoogleFeedProduct = { id: 123 };
          expectedResult = service.addGoogleFeedProductToCollectionIfMissing([], googleFeedProduct);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(googleFeedProduct);
        });

        it('should not add a GoogleFeedProduct to an array that contains it', () => {
          const googleFeedProduct: IGoogleFeedProduct = { id: 123 };
          const googleFeedProductCollection: IGoogleFeedProduct[] = [
            {
              ...googleFeedProduct,
            },
            { id: 456 },
          ];
          expectedResult = service.addGoogleFeedProductToCollectionIfMissing(googleFeedProductCollection, googleFeedProduct);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a GoogleFeedProduct to an array that doesn't contain it", () => {
          const googleFeedProduct: IGoogleFeedProduct = { id: 123 };
          const googleFeedProductCollection: IGoogleFeedProduct[] = [{ id: 456 }];
          expectedResult = service.addGoogleFeedProductToCollectionIfMissing(googleFeedProductCollection, googleFeedProduct);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(googleFeedProduct);
        });

        it('should add only unique GoogleFeedProduct to an array', () => {
          const googleFeedProductArray: IGoogleFeedProduct[] = [{ id: 123 }, { id: 456 }, { id: 25687 }];
          const googleFeedProductCollection: IGoogleFeedProduct[] = [{ id: 123 }];
          expectedResult = service.addGoogleFeedProductToCollectionIfMissing(googleFeedProductCollection, ...googleFeedProductArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const googleFeedProduct: IGoogleFeedProduct = { id: 123 };
          const googleFeedProduct2: IGoogleFeedProduct = { id: 456 };
          expectedResult = service.addGoogleFeedProductToCollectionIfMissing([], googleFeedProduct, googleFeedProduct2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(googleFeedProduct);
          expect(expectedResult).toContain(googleFeedProduct2);
        });

        it('should accept null and undefined values', () => {
          const googleFeedProduct: IGoogleFeedProduct = { id: 123 };
          expectedResult = service.addGoogleFeedProductToCollectionIfMissing([], null, googleFeedProduct, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(googleFeedProduct);
        });

        it('should return initial array if no GoogleFeedProduct is added', () => {
          const googleFeedProductCollection: IGoogleFeedProduct[] = [{ id: 123 }];
          expectedResult = service.addGoogleFeedProductToCollectionIfMissing(googleFeedProductCollection, undefined, null);
          expect(expectedResult).toEqual(googleFeedProductCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
