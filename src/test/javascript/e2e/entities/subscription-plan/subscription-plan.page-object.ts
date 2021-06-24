import { element, by, ElementFinder } from 'protractor';

export class SubscriptionPlanComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('stl-subscription-plan div table .btn-danger'));
  title = element.all(by.css('stl-subscription-plan div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('stlTranslate');
  }
}

export class SubscriptionPlanUpdatePage {
  pageTitle = element(by.id('stl-subscription-plan-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nameInput = element(by.id('field_name'));
  descriptionInput = element(by.id('field_description'));
  categorySelect = element(by.id('field_category'));
  maxProductsInput = element(by.id('field_maxProducts'));
  maxSpacesInput = element(by.id('field_maxSpaces'));
  maxRequestsInput = element(by.id('field_maxRequests'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('stlTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setNameInput(name: string): Promise<void> {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput(): Promise<string> {
    return await this.nameInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setCategorySelect(category: string): Promise<void> {
    await this.categorySelect.sendKeys(category);
  }

  async getCategorySelect(): Promise<string> {
    return await this.categorySelect.element(by.css('option:checked')).getText();
  }

  async categorySelectLastOption(): Promise<void> {
    await this.categorySelect.all(by.tagName('option')).last().click();
  }

  async setMaxProductsInput(maxProducts: string): Promise<void> {
    await this.maxProductsInput.sendKeys(maxProducts);
  }

  async getMaxProductsInput(): Promise<string> {
    return await this.maxProductsInput.getAttribute('value');
  }

  async setMaxSpacesInput(maxSpaces: string): Promise<void> {
    await this.maxSpacesInput.sendKeys(maxSpaces);
  }

  async getMaxSpacesInput(): Promise<string> {
    return await this.maxSpacesInput.getAttribute('value');
  }

  async setMaxRequestsInput(maxRequests: string): Promise<void> {
    await this.maxRequestsInput.sendKeys(maxRequests);
  }

  async getMaxRequestsInput(): Promise<string> {
    return await this.maxRequestsInput.getAttribute('value');
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class SubscriptionPlanDeleteDialog {
  private dialogTitle = element(by.id('stl-delete-subscriptionPlan-heading'));
  private confirmButton = element(by.id('stl-confirm-delete-subscriptionPlan'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('stlTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
