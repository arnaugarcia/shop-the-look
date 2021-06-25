import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICoordinate } from '../coordinate.model';
import { CoordinateService } from '../service/coordinate.service';
import { CoordinateDeleteDialogComponent } from '../delete/coordinate-delete-dialog.component';

@Component({
  selector: 'stl-coordinate',
  templateUrl: './coordinate.component.html',
})
export class CoordinateComponent implements OnInit {
  coordinates?: ICoordinate[];
  isLoading = false;

  constructor(protected coordinateService: CoordinateService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.coordinateService.query().subscribe(
      (res: HttpResponse<ICoordinate[]>) => {
        this.isLoading = false;
        this.coordinates = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICoordinate): number {
    return item.id!;
  }

  delete(coordinate: ICoordinate): void {
    const modalRef = this.modalService.open(CoordinateDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.coordinate = coordinate;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
