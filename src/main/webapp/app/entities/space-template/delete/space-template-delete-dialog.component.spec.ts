jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { SpaceTemplateService } from '../service/space-template.service';

import { SpaceTemplateDeleteDialogComponent } from './space-template-delete-dialog.component';

describe('Component Tests', () => {
  describe('SpaceTemplate Management Delete Component', () => {
    let comp: SpaceTemplateDeleteDialogComponent;
    let fixture: ComponentFixture<SpaceTemplateDeleteDialogComponent>;
    let service: SpaceTemplateService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SpaceTemplateDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(SpaceTemplateDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SpaceTemplateDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(SpaceTemplateService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        jest.spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
