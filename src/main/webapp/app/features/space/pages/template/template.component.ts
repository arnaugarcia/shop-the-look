import { Component, OnInit } from '@angular/core';
import { StudioService, StudioTemplates } from '../../service/studio.service';
import { SpaceService } from '../../service/space.service';
import { ActivatedRoute } from '@angular/router';
import { ISpace } from '../../model/space.model';

@Component({
  selector: 'stl-template',
  templateUrl: './template.component.html',
  styleUrls: ['./template.component.scss'],
})
export class TemplateComponent implements OnInit {
  public selectedOption: StudioTemplates;
  public readonly StudioTemplates = StudioTemplates;
  private space?: ISpace;

  constructor(private activatedRoute: ActivatedRoute, private studioService: StudioService, private spaceService: SpaceService) {
    this.studioService.navigate('template');
    this.selectedOption = studioService.data.template;
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ space }) => {
      this.updateForm(space);
      this.space = space;
    });
  }

  selectOption(option: StudioTemplates): void {
    this.selectedOption = option;
    this.studioService.setTemplate(option);
    if (this.space) {
      this.space.template = option.valueOf();
      this.spaceService.update(this.space).subscribe();
    }
  }

  private updateForm(space: ISpace): void {
    this.selectedOption = (<any>StudioTemplates)[space.template!];
  }
}
