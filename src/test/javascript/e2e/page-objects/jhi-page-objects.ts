import { element, by, ElementFinder } from 'protractor';

/* eslint @typescript-eslint/no-use-before-define: 0 */
export class NavBarPage {
  entityMenu = element(by.id('entity-menu'));
  accountMenu = element(by.id('account-menu'));
  adminMenu!: ElementFinder;
  signIn = element(by.id('login'));
  register = element(by.css('[routerLink="account/register"]'));
  signOut = element(by.id('logout'));
  passwordMenu = element(by.css('[routerLink="account/password"]'));
  settingsMenu = element(by.css('[routerLink="account/settings"]'));

  constructor(asAdmin?: boolean) {
    if (asAdmin) {
      this.adminMenu = element(by.id('admin-menu'));
    }
  }

  async clickOnEntityMenu(): Promise<void> {
    await this.entityMenu.click();
  }

  async clickOnAccountMenu(): Promise<void> {
    await this.accountMenu.click();
  }

  async clickOnAdminMenu(): Promise<void> {
    await this.adminMenu.click();
  }

  async clickOnSignIn(): Promise<void> {
    await this.signIn.click();
  }

  async clickOnRegister(): Promise<void> {
    await this.signIn.click();
  }

  async clickOnSignOut(): Promise<void> {
    await this.signOut.click();
  }

  async clickOnPasswordMenu(): Promise<void> {
    await this.passwordMenu.click();
  }

  async clickOnSettingsMenu(): Promise<void> {
    await this.settingsMenu.click();
  }

  async clickOnEntity(entityName: string): Promise<void> {
    await element(by.css('[routerLink="' + entityName + '"]')).click();
  }

  async clickOnAdmin(entityName: string): Promise<void> {
    await element(by.css('[routerLink="admin/' + entityName + '"]')).click();
  }

  async getSignInPage(): Promise<SignInPage> {
    await this.clickOnAccountMenu();
    await this.clickOnSignIn();
    return new SignInPage();
  }

  async getPasswordPage(): Promise<PasswordPage> {
    await this.clickOnAccountMenu();
    await this.clickOnPasswordMenu();
    return new PasswordPage();
  }

  async getSettingsPage(): Promise<SettingsPage> {
    await this.clickOnAccountMenu();
    await this.clickOnSettingsMenu();
    return new SettingsPage();
  }

  async goToEntity(entityName: string): Promise<void> {
    await this.clickOnEntityMenu();
    await this.clickOnEntity(entityName);
  }

  async goToSignInPage(): Promise<void> {
    await this.clickOnAccountMenu();
    await this.clickOnSignIn();
  }

  async goToPasswordMenu(): Promise<void> {
    await this.clickOnAccountMenu();
    await this.clickOnPasswordMenu();
  }

  async autoSignOut(): Promise<void> {
    await this.clickOnAccountMenu();
    await this.clickOnSignOut();
  }
}

export class SignInPage {
  username = element(by.id('username'));
  password = element(by.id('password'));
  loginButton = element(by.css('button[type=submit]'));

  async setUserName(username: string): Promise<void> {
    await this.username.sendKeys(username);
  }

  async getUserName(): Promise<string> {
    return this.username.getAttribute('value');
  }

  async clearUserName(): Promise<void> {
    await this.username.clear();
  }

  async setPassword(password: string): Promise<void> {
    await this.password.sendKeys(password);
  }

  async getPassword(): Promise<string> {
    return this.password.getAttribute('value');
  }

  async clearPassword(): Promise<void> {
    await this.password.clear();
  }

  async autoSignInUsing(username: string, password: string): Promise<void> {
    await this.setUserName(username);
    await this.setPassword(password);
    await this.login();
  }

  async login(): Promise<void> {
    await this.loginButton.click();
  }
}

export class PasswordPage {
  currentPassword = element(by.id('currentPassword'));
  password = element(by.id('newPassword'));
  confirmPassword = element(by.id('confirmPassword'));
  saveButton = element(by.css('button[type=submit]'));
  title = element.all(by.css('h2')).first();

  async setCurrentPassword(password: string): Promise<void> {
    await this.currentPassword.sendKeys(password);
  }

  async setPassword(password: string): Promise<void> {
    await this.password.sendKeys(password);
  }

  async getPassword(): Promise<string> {
    return this.password.getAttribute('value');
  }

  async clearPassword(): Promise<void> {
    await this.password.clear();
  }

  async setConfirmPassword(confirmPassword: string): Promise<void> {
    await this.confirmPassword.sendKeys(confirmPassword);
  }

  async getConfirmPassword(): Promise<string> {
    return this.confirmPassword.getAttribute('value');
  }

  async clearConfirmPassword(): Promise<void> {
    await this.confirmPassword.clear();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('stlTranslate');
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }
}

export class SettingsPage {
  firstName = element(by.id('firstName'));
  lastName = element(by.id('lastName'));
  email = element(by.id('email'));
  saveButton = element(by.css('button[type=submit]'));
  title = element.all(by.css('h2')).first();

  async setFirstName(firstName: string): Promise<void> {
    await this.firstName.sendKeys(firstName);
  }

  async getFirstName(): Promise<string> {
    return this.firstName.getAttribute('value');
  }

  async clearFirstName(): Promise<void> {
    await this.firstName.clear();
  }

  async setLastName(lastName: string): Promise<void> {
    await this.lastName.sendKeys(lastName);
  }

  async getLastName(): Promise<string> {
    return this.lastName.getAttribute('value');
  }

  async clearLastName(): Promise<void> {
    await this.lastName.clear();
  }

  async setEmail(email: string): Promise<void> {
    await this.email.sendKeys(email);
  }

  async getEmail(): Promise<string> {
    return this.email.getAttribute('value');
  }

  async clearEmail(): Promise<void> {
    await this.email.clear();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('stlTranslate');
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }
}
