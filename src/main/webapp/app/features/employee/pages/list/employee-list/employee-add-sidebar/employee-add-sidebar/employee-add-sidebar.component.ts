import { Component } from '@angular/core';
import { CoreSidebarService } from '../../../../../../../../@core/components/core-sidebar/core-sidebar.service';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'stl-employee-add-sidebar',
  templateUrl: './employee-add-sidebar.component.html',
  styleUrls: ['./employee-add-sidebar.component.scss'],
})
export class EmployeeAddSidebarComponent {
  public fullname = '';
  public username = '';
  public email = '';

  /**
   * Constructor
   *
   * @param {CoreSidebarService} _coreSidebarService
   */
  constructor(private _coreSidebarService: CoreSidebarService) {}

  /**
   * Toggle the sidebar
   *
   * @param name
   */
  toggleSidebar(name: string): void {
    this._coreSidebarService.getSidebarRegistry(name).toggleOpen();
  }

  /**
   * Submit
   *
   * @param form
   */
  submit(form: NgForm): void {
    if (form.valid) {
      this.toggleSidebar('new-user-sidebar');
    }
  }
}
