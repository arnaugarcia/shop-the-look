export interface IEmployee {
  firstName?: string;
  lastName?: string;
  login?: string;
  email?: string;
  authorities?: string[];
}

export class Employee implements IEmployee {
  constructor(
    public firstName?: string,
    public lastName?: string,
    public login?: string,
    public email?: string,
    public authorities?: string[]
  ) {}
}
