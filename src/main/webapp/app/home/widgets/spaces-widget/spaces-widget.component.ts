import { Component, OnInit } from '@angular/core';
import { StatisticsService } from '../../services/statistics.service';
import { Space } from '../../models/statistics.model';
import { HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'stl-spaces-widget',
  templateUrl: './spaces-widget.component.html',
})
export class SpacesWidgetComponent implements OnInit {
  public spaces: Space[] = [];

  constructor(private statisticsService: StatisticsService, private router: Router) {}

  ngOnInit(): void {
    this.statisticsService.querySpacesStats().subscribe((response: HttpResponse<Space[]>) => {
      if (response.body) {
        this.spaces = response.body;
      }
    });
  }

  clickSpace(space: Space): void {
    this.router.navigate(['spaces', 'studio', space.reference, 'edit']);
  }
}
