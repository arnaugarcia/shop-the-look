import { Component, OnInit } from '@angular/core';
import { StatisticsService } from '../../services/statistics.service';
import { GeneralStatus } from '../../models/statistics.model';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'stl-general-statistics-widget',
  templateUrl: './general-statistics-widget.component.html',
  styleUrls: ['./general-statistics-widget.component.scss'],
})
export class GeneralStatisticsWidgetComponent implements OnInit {
  public statistics?: GeneralStatus;

  constructor(private statisticsService: StatisticsService) {}

  ngOnInit(): void {
    this.statisticsService.queryGeneralStats().subscribe((response: HttpResponse<GeneralStatus>) => {
      /*if (response.body) {
        this.statistics = response.body;
      }*/
    });
  }
}
