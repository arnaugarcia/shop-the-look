import { Component, OnInit } from '@angular/core';
import { StudioService, StudioTemplate } from '../../service/studio.service';

@Component({
  selector: 'stl-customize',
  templateUrl: './customize.component.html',
  styleUrls: ['./customize.component.scss'],
})
export class CustomizeComponent implements OnInit {
  public template: StudioTemplate;
  public StudioTemplate = StudioTemplate;

  constructor(private studioService: StudioService) {
    this.studioService.navigate('customize');
    this.template = this.studioService.data.template;
  }

  ngOnInit(): void {
    this.template = this.studioService.data.template;
  }
}
