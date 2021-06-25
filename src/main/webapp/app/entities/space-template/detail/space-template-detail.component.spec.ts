import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SpaceTemplateDetailComponent } from './space-template-detail.component';

describe('Component Tests', () => {
  describe('SpaceTemplate Management Detail Component', () => {
    let comp: SpaceTemplateDetailComponent;
    let fixture: ComponentFixture<SpaceTemplateDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [SpaceTemplateDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ spaceTemplate: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(SpaceTemplateDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SpaceTemplateDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load spaceTemplate on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.spaceTemplate).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
