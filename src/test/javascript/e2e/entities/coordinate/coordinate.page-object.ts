import { element, by, ElementFinder } from 'protractor';

export class CoordinateComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('stl-coordinate div table .btn-danger'));
  title = element.all(by.css('stl-coordinate div h2#page-heading span')).first();
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

export class CoordinateUpdatePage {
  pageTitle = element(by.id('stl-coordinate-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  xInput = element(by.id('field_x'));
  yInput = element(by.id('field_y'));

  photoSelect = element(by.id('field_photo'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('stlTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setXInput(x: string): Promise<void> {
    await this.xInput.sendKeys(x);
  }

  async getXInput(): Promise<string> {
    return await this.xInput.getAttribute('value');
  }

  async setYInput(y: string): Promise<void> {
    await this.yInput.sendKeys(y);
  }

  async getYInput(): Promise<string> {
    return await this.yInput.getAttribute('value');
  }

  async photoSelectLastOption(): Promise<void> {
    await this.photoSelect.all(by.tagName('option')).last().click();
  }

  async photoSelectOption(option: string): Promise<void> {
    await this.photoSelect.sendKeys(option);
  }

  getPhotoSelect(): ElementFinder {
    return this.photoSelect;
  }

  async getPhotoSelectedOption(): Promise<string> {
    return await this.photoSelect.element(by.css('option:checked')).getText();
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

export class CoordinateDeleteDialog {
  private dialogTitle = element(by.id('stl-delete-coordinate-heading'));
  private confirmButton = element(by.id('stl-confirm-delete-coordinate'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('stlTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
