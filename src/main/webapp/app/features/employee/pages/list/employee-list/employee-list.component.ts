import { Component } from '@angular/core';
import { CoreSidebarService } from '../../../../../../@core/components/core-sidebar/core-sidebar.service';

@Component({
  selector: 'stl-employee-list',
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.scss'],
})
export class EmployeeListComponent {
  // Public
  public selectedOption = 10;
  public searchValue = '';

  constructor(private coreSidebarService: CoreSidebarService) {}

  /**
   * Toggle the sidebar
   *
   * @param name
   */
  toggleSidebar(name: string): void {
    this.coreSidebarService.getSidebarRegistry(name).toggleOpen();
  }
}
