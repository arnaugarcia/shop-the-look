import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISpaceTemplate } from '../space-template.model';
import { SpaceTemplateService } from '../service/space-template.service';
import { SpaceTemplateDeleteDialogComponent } from '../delete/space-template-delete-dialog.component';

@Component({
  selector: 'stl-space-template',
  templateUrl: './space-template.component.html',
})
export class SpaceTemplateComponent implements OnInit {
  spaceTemplates?: ISpaceTemplate[];
  isLoading = false;

  constructor(protected spaceTemplateService: SpaceTemplateService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.spaceTemplateService.query().subscribe(
      (res: HttpResponse<ISpaceTemplate[]>) => {
        this.isLoading = false;
        this.spaceTemplates = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ISpaceTemplate): number {
    return item.id!;
  }

  delete(spaceTemplate: ISpaceTemplate): void {
    const modalRef = this.modalService.open(SpaceTemplateDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.spaceTemplate = spaceTemplate;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
