import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CoordinateDetailComponent } from './coordinate-detail.component';

describe('Component Tests', () => {
  describe('Coordinate Management Detail Component', () => {
    let comp: CoordinateDetailComponent;
    let fixture: ComponentFixture<CoordinateDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CoordinateDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ coordinate: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CoordinateDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CoordinateDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load coordinate on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.coordinate).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
