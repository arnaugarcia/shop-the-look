import { Component } from '@angular/core';
import { ContentHeader } from '../../../../layouts/content-header/content-header.component';
import { ActivatedRoute, Router } from '@angular/router';
import { ISpace } from '../../model/space.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SpaceService } from '../../service/space.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { SpaceDeleteDialogComponent } from '../../component/space-delete-dialog/space-delete-dialog.component';

@Component({
  selector: 'stl-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss'],
})
export class ListComponent {
  public spaces: ISpace[] = [];
  public contentHeader: ContentHeader;

  constructor(private spaceService: SpaceService, private router: Router, private route: ActivatedRoute, private modalService: NgbModal) {
    this.contentHeader = {
      headerTitle: 'Spaces',
      actions: [
        {
          name: 'New Space',
          link: '/spaces/studio',
          icon: 'plus',
        },
      ],
      breadcrumb: {
        type: '',
        links: [
          {
            name: 'Home',
            isLink: true,
            link: '/',
          },
          {
            name: 'Spaces',
            isLink: false,
          },
        ],
      },
    };
    this.route.data.subscribe(({ spaces }) => {
      this.spaces = spaces;
    });
  }

  public removeSpace(space: ISpace): void {
    const modalRef = this.modalService.open(SpaceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.space = space;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      } else {
        console.error(reason);
      }
    });
  }

  public onEditSpace(space: ISpace): void {
    this.router.navigate(['spaces', 'studio', space.reference, 'edit']);
  }

  private loadPage(): void {
    this.spaceService.queryForCurrentUser().subscribe(
      (response: HttpResponse<ISpace[]>) => {
        this.spaces = response.body!;
      },
      (error: HttpErrorResponse) => {
        console.error(error);
      }
    );
  }
}
