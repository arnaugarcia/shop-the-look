import { Component, OnInit } from '@angular/core';
import { SpaceService } from '../../service/space.service';
import { ActivatedRoute } from '@angular/router';
import { ISpace } from '../../model/space.model';
import { StudioStore } from '../../store/studio.store';
import { StudioTemplate } from '../../store/models/state.model';

@Component({
  selector: 'stl-template',
  templateUrl: './template.component.html',
  styleUrls: ['./template.component.scss'],
})
export class TemplateComponent implements OnInit {
  public selectedTemplate?: StudioTemplate;
  public readonly StudioTemplates = StudioTemplate;
  private space?: ISpace;

  constructor(private activatedRoute: ActivatedRoute, private studioStore: StudioStore, private spaceService: SpaceService) {
    this.studioStore.navigate('template');
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ space }) => {
      this.updateForm(space);
      this.space = space;
    });
    this.studioStore.template$.subscribe((selectedTemplate: StudioTemplate) => {
      this.selectedTemplate = selectedTemplate;
    });
  }

  selectOption(option: StudioTemplate): void {
    this.selectedTemplate = option;
    this.studioStore.template(option);
    if (this.space) {
      this.space.template = option.valueOf();
      this.spaceService.update(this.space).subscribe();
    }
  }

  private updateForm(space: ISpace): void {
    this.selectedTemplate = (<any>StudioTemplate)[space.template];
  }
}
