import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBillingAddress, BillingAddress } from '../billing-address.model';

import { BillingAddressService } from './billing-address.service';

describe('Service Tests', () => {
  describe('BillingAddress Service', () => {
    let service: BillingAddressService;
    let httpMock: HttpTestingController;
    let elemDefault: IBillingAddress;
    let expectedResult: IBillingAddress | IBillingAddress[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(BillingAddressService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        address: 'AAAAAAA',
        city: 'AAAAAAA',
        province: 'AAAAAAA',
        zipCode: 'AAAAAAA',
        country: 'AAAAAAA',
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

      it('should create a BillingAddress', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new BillingAddress()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a BillingAddress', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            address: 'BBBBBB',
            city: 'BBBBBB',
            province: 'BBBBBB',
            zipCode: 'BBBBBB',
            country: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a BillingAddress', () => {
        const patchObject = Object.assign(
          {
            province: 'BBBBBB',
            zipCode: 'BBBBBB',
          },
          new BillingAddress()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of BillingAddress', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            address: 'BBBBBB',
            city: 'BBBBBB',
            province: 'BBBBBB',
            zipCode: 'BBBBBB',
            country: 'BBBBBB',
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

      it('should delete a BillingAddress', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addBillingAddressToCollectionIfMissing', () => {
        it('should add a BillingAddress to an empty array', () => {
          const billingAddress: IBillingAddress = { id: 123 };
          expectedResult = service.addBillingAddressToCollectionIfMissing([], billingAddress);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(billingAddress);
        });

        it('should not add a BillingAddress to an array that contains it', () => {
          const billingAddress: IBillingAddress = { id: 123 };
          const billingAddressCollection: IBillingAddress[] = [
            {
              ...billingAddress,
            },
            { id: 456 },
          ];
          expectedResult = service.addBillingAddressToCollectionIfMissing(billingAddressCollection, billingAddress);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a BillingAddress to an array that doesn't contain it", () => {
          const billingAddress: IBillingAddress = { id: 123 };
          const billingAddressCollection: IBillingAddress[] = [{ id: 456 }];
          expectedResult = service.addBillingAddressToCollectionIfMissing(billingAddressCollection, billingAddress);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(billingAddress);
        });

        it('should add only unique BillingAddress to an array', () => {
          const billingAddressArray: IBillingAddress[] = [{ id: 123 }, { id: 456 }, { id: 67204 }];
          const billingAddressCollection: IBillingAddress[] = [{ id: 123 }];
          expectedResult = service.addBillingAddressToCollectionIfMissing(billingAddressCollection, ...billingAddressArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const billingAddress: IBillingAddress = { id: 123 };
          const billingAddress2: IBillingAddress = { id: 456 };
          expectedResult = service.addBillingAddressToCollectionIfMissing([], billingAddress, billingAddress2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(billingAddress);
          expect(expectedResult).toContain(billingAddress2);
        });

        it('should accept null and undefined values', () => {
          const billingAddress: IBillingAddress = { id: 123 };
          expectedResult = service.addBillingAddressToCollectionIfMissing([], null, billingAddress, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(billingAddress);
        });

        it('should return initial array if no BillingAddress is added', () => {
          const billingAddressCollection: IBillingAddress[] = [{ id: 123 }];
          expectedResult = service.addBillingAddressToCollectionIfMissing(billingAddressCollection, undefined, null);
          expect(expectedResult).toEqual(billingAddressCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
