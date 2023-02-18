export enum AccountStatus {
  ACTIVE = 'ACTIVE',
  DISABLED = 'DISABLED',
  PENDING = 'PENDING',
}

export interface IEmployee {
  firstName?: string;
  lastName?: string;
  login?: string;
  status?: AccountStatus;
  imageUrl?: string;
  email?: string;
  authorities?: string[];
  companyReference?: string;
}

export class Employee implements IEmployee {
  constructor(
    public firstName: string,
    public lastName: string,
    public imageUrl: string,
    public status: AccountStatus,
    public login: string,
    public email: string,
    public authorities: string[],
    public companyReference: string
  ) {}
}

export class EmployeeRequest implements IEmployee {
  constructor(
    public firstName?: string,
    public lastName?: string,
    public imageUrl?: string,
    public status?: AccountStatus,
    public login?: string,
    public email?: string,
    public authorities?: string[],
    public companyReference?: string
  ) {}
}
