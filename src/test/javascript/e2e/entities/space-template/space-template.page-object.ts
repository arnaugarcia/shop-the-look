import { element, by, ElementFinder } from 'protractor';

export class SpaceTemplateComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('stl-space-template div table .btn-danger'));
  title = element.all(by.css('stl-space-template div h2#page-heading span')).first();
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

export class SpaceTemplateUpdatePage {
  pageTitle = element(by.id('stl-space-template-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nameInput = element(by.id('field_name'));
  descriptionInput = element(by.id('field_description'));
  maxProductsInput = element(by.id('field_maxProducts'));
  maxPhotosInput = element(by.id('field_maxPhotos'));
  activeInput = element(by.id('field_active'));

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

  async setMaxProductsInput(maxProducts: string): Promise<void> {
    await this.maxProductsInput.sendKeys(maxProducts);
  }

  async getMaxProductsInput(): Promise<string> {
    return await this.maxProductsInput.getAttribute('value');
  }

  async setMaxPhotosInput(maxPhotos: string): Promise<void> {
    await this.maxPhotosInput.sendKeys(maxPhotos);
  }

  async getMaxPhotosInput(): Promise<string> {
    return await this.maxPhotosInput.getAttribute('value');
  }

  getActiveInput(): ElementFinder {
    return this.activeInput;
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

export class SpaceTemplateDeleteDialog {
  private dialogTitle = element(by.id('stl-delete-spaceTemplate-heading'));
  private confirmButton = element(by.id('stl-confirm-delete-spaceTemplate'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('stlTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
