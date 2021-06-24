import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISpace } from '../space.model';
import { SpaceService } from '../service/space.service';
import { SpaceDeleteDialogComponent } from '../delete/space-delete-dialog.component';

@Component({
  selector: 'stl-space',
  templateUrl: './space.component.html',
})
export class SpaceComponent implements OnInit {
  spaces?: ISpace[];
  isLoading = false;

  constructor(protected spaceService: SpaceService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.spaceService.query().subscribe(
      (res: HttpResponse<ISpace[]>) => {
        this.isLoading = false;
        this.spaces = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ISpace): number {
    return item.id!;
  }

  delete(space: ISpace): void {
    const modalRef = this.modalService.open(SpaceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.space = space;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
