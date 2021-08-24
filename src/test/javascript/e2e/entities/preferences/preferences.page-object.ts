import { by, element, ElementFinder } from 'protractor';

export class PreferencesComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('stl-preferences div table .btn-danger'));
  title = element.all(by.css('stl-preferences div h2#page-heading span')).first();
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

export class PreferencesUpdatePage {
  pageTitle = element(by.id('stl-preferences-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  importMethodSelect = element(by.id('field_importMethod'));
  feedUrlInput = element(by.id('field_feedUrl'));
  remainingImportsInput = element(by.id('field_remainingImports'));
  lastImportByInput = element(by.id('field_lastImportBy'));
  lastImportTimestampInput = element(by.id('field_lastImportTimestamp'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('stlTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setImportMethodSelect(importMethod: string): Promise<void> {
    await this.importMethodSelect.sendKeys(importMethod);
  }

  async getImportMethodSelect(): Promise<string> {
    return await this.importMethodSelect.element(by.css('option:checked')).getText();
  }

  async importMethodSelectLastOption(): Promise<void> {
    await this.importMethodSelect.all(by.tagName('option')).last().click();
  }

  async setFeedUrlInput(feedUrl: string): Promise<void> {
    await this.feedUrlInput.sendKeys(feedUrl);
  }

  async getFeedUrlInput(): Promise<string> {
    return await this.feedUrlInput.getAttribute('value');
  }

  async setRemainingImportsInput(remainingImports: string): Promise<void> {
    await this.remainingImportsInput.sendKeys(remainingImports);
  }

  async getRemainingImportsInput(): Promise<string> {
    return await this.remainingImportsInput.getAttribute('value');
  }

  async setLastImportByInput(lastImportBy: string): Promise<void> {
    await this.lastImportByInput.sendKeys(lastImportBy);
  }

  async getLastImportByInput(): Promise<string> {
    return await this.lastImportByInput.getAttribute('value');
  }

  async setLastImportTimestampInput(lastImportTimestamp: string): Promise<void> {
    await this.lastImportTimestampInput.sendKeys(lastImportTimestamp);
  }

  async getLastImportTimestampInput(): Promise<string> {
    return await this.lastImportTimestampInput.getAttribute('value');
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

export class PreferencesDeleteDialog {
  private dialogTitle = element(by.id('stl-delete-preferences-heading'));
  private confirmButton = element(by.id('stl-confirm-delete-preferences'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('stlTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
