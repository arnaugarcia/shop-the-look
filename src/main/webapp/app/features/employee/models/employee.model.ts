export interface IEmployee {
  firstname?: string;
  surname?: string;
  login?: string;
}

export class Employee implements IEmployee {
  constructor(public firstname?: string, public surname?: string, public login?: string) {}
}
