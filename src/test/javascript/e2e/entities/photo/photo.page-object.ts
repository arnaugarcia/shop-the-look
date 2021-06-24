import { element, by, ElementFinder } from 'protractor';

export class PhotoComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('stl-photo div table .btn-danger'));
  title = element.all(by.css('stl-photo div h2#page-heading span')).first();
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

export class PhotoUpdatePage {
  pageTitle = element(by.id('stl-photo-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nameInput = element(by.id('field_name'));
  descriptionInput = element(by.id('field_description'));
  linkInput = element(by.id('field_link'));
  orderInput = element(by.id('field_order'));
  heightInput = element(by.id('field_height'));
  widthInput = element(by.id('field_width'));
  orientationSelect = element(by.id('field_orientation'));
  demoInput = element(by.id('field_demo'));

  spaceSelect = element(by.id('field_space'));
  spaceTemplateSelect = element(by.id('field_spaceTemplate'));

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

  async setLinkInput(link: string): Promise<void> {
    await this.linkInput.sendKeys(link);
  }

  async getLinkInput(): Promise<string> {
    return await this.linkInput.getAttribute('value');
  }

  async setOrderInput(order: string): Promise<void> {
    await this.orderInput.sendKeys(order);
  }

  async getOrderInput(): Promise<string> {
    return await this.orderInput.getAttribute('value');
  }

  async setHeightInput(height: string): Promise<void> {
    await this.heightInput.sendKeys(height);
  }

  async getHeightInput(): Promise<string> {
    return await this.heightInput.getAttribute('value');
  }

  async setWidthInput(width: string): Promise<void> {
    await this.widthInput.sendKeys(width);
  }

  async getWidthInput(): Promise<string> {
    return await this.widthInput.getAttribute('value');
  }

  async setOrientationSelect(orientation: string): Promise<void> {
    await this.orientationSelect.sendKeys(orientation);
  }

  async getOrientationSelect(): Promise<string> {
    return await this.orientationSelect.element(by.css('option:checked')).getText();
  }

  async orientationSelectLastOption(): Promise<void> {
    await this.orientationSelect.all(by.tagName('option')).last().click();
  }

  getDemoInput(): ElementFinder {
    return this.demoInput;
  }

  async spaceSelectLastOption(): Promise<void> {
    await this.spaceSelect.all(by.tagName('option')).last().click();
  }

  async spaceSelectOption(option: string): Promise<void> {
    await this.spaceSelect.sendKeys(option);
  }

  getSpaceSelect(): ElementFinder {
    return this.spaceSelect;
  }

  async getSpaceSelectedOption(): Promise<string> {
    return await this.spaceSelect.element(by.css('option:checked')).getText();
  }

  async spaceTemplateSelectLastOption(): Promise<void> {
    await this.spaceTemplateSelect.all(by.tagName('option')).last().click();
  }

  async spaceTemplateSelectOption(option: string): Promise<void> {
    await this.spaceTemplateSelect.sendKeys(option);
  }

  getSpaceTemplateSelect(): ElementFinder {
    return this.spaceTemplateSelect;
  }

  async getSpaceTemplateSelectedOption(): Promise<string> {
    return await this.spaceTemplateSelect.element(by.css('option:checked')).getText();
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

export class PhotoDeleteDialog {
  private dialogTitle = element(by.id('stl-delete-photo-heading'));
  private confirmButton = element(by.id('stl-confirm-delete-photo'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('stlTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
