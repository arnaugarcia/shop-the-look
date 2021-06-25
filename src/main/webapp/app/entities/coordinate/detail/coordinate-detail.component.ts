import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICoordinate } from '../coordinate.model';

@Component({
  selector: 'stl-coordinate-detail',
  templateUrl: './coordinate-detail.component.html',
})
export class CoordinateDetailComponent implements OnInit {
  coordinate: ICoordinate | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ coordinate }) => {
      this.coordinate = coordinate;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
