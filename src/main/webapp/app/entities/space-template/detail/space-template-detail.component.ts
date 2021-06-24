import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISpaceTemplate } from '../space-template.model';

@Component({
  selector: 'stl-space-template-detail',
  templateUrl: './space-template-detail.component.html',
})
export class SpaceTemplateDetailComponent implements OnInit {
  spaceTemplate: ISpaceTemplate | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ spaceTemplate }) => {
      this.spaceTemplate = spaceTemplate;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
