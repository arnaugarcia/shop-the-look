import { Component } from '@angular/core';

@Component({
  selector: 'stl-employee-list',
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.scss'],
})
export class EmployeeListComponent {
  // Public
  public selectedOption = 10;
  public searchValue = '';
}
