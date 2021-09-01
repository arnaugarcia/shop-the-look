import { Component } from '@angular/core';
import { StudioService, StudioTemplates } from '../../service/studio.service';

@Component({
  selector: 'stl-template',
  templateUrl: './template.component.html',
  styleUrls: ['./template.component.scss'],
})
export class TemplateComponent {
  public selectedOption: StudioTemplates;

  constructor(private studioService: StudioService) {
    this.studioService.navigate('template');
    this.selectedOption = studioService.data.template;
  }

  selectOption(option: StudioTemplates): void {
    this.selectedOption = option;
    this.studioService.setTemplate(option);
  }
}
