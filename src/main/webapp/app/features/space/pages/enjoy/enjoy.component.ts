import { Component } from '@angular/core';
import { StudioStore } from '../../store/studio.store';
import { snippetCode } from '../../../../../@core/components/card-snippet/card-snippet.component';
import { ActivatedRoute } from '@angular/router';
import { ISpace } from '../../model/space.model';
import { ICompany } from '../../../company/model/company.model';

@Component({
  selector: 'stl-enjoy',
  templateUrl: './enjoy.component.html',
  styleUrls: ['./enjoy.component.scss'],
})
export class EnjoyComponent {
  public snippetCode?: snippetCode;

  private space?: ISpace;
  private company?: ICompany;

  constructor(private studioStore: StudioStore, private route: ActivatedRoute) {
    studioStore.navigate('enjoy');
    this.route.data.subscribe(({ space, company }) => {
      this.space = space;
      this.company = company;
      this.snippetCode = {
        html: `
<script async defer src='https://cdn.shopthelook.com/client/shopthelook.js?v=1.0&key=${this.company!.token!}'></script>
<div id='space-${this.space!.reference}'></div>
`,
        isCollapsed: false,
      };
    });
  }
}
