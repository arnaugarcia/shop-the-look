import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GoogleFeedProductDetailComponent } from './google-feed-product-detail.component';

describe('Component Tests', () => {
  describe('GoogleFeedProduct Management Detail Component', () => {
    let comp: GoogleFeedProductDetailComponent;
    let fixture: ComponentFixture<GoogleFeedProductDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [GoogleFeedProductDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ googleFeedProduct: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(GoogleFeedProductDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GoogleFeedProductDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load googleFeedProduct on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.googleFeedProduct).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
