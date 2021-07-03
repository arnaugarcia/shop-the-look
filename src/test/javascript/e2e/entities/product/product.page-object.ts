import { element, by, ElementFinder } from 'protractor';

export class ProductComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('stl-product div table .btn-danger'));
  title = element.all(by.css('stl-product div h2#page-heading span')).first();
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

export class ProductUpdatePage {
  pageTitle = element(by.id('stl-product-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  skuInput = element(by.id('field_sku'));
  nameInput = element(by.id('field_name'));
  descriptionInput = element(by.id('field_description'));
  linkInput = element(by.id('field_link'));
  imageLinkInput = element(by.id('field_imageLink'));
  additionalImageLinkInput = element(by.id('field_additionalImageLink'));
  availabilitySelect = element(by.id('field_availability'));
  priceInput = element(by.id('field_price'));
  categoryInput = element(by.id('field_category'));

  companySelect = element(by.id('field_company'));
  coordinateSelect = element(by.id('field_coordinate'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('stlTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setSkuInput(sku: string): Promise<void> {
    await this.skuInput.sendKeys(sku);
  }

  async getSkuInput(): Promise<string> {
    return await this.skuInput.getAttribute('value');
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

  async setImageLinkInput(imageLink: string): Promise<void> {
    await this.imageLinkInput.sendKeys(imageLink);
  }

  async getImageLinkInput(): Promise<string> {
    return await this.imageLinkInput.getAttribute('value');
  }

  async setAdditionalImageLinkInput(additionalImageLink: string): Promise<void> {
    await this.additionalImageLinkInput.sendKeys(additionalImageLink);
  }

  async getAdditionalImageLinkInput(): Promise<string> {
    return await this.additionalImageLinkInput.getAttribute('value');
  }

  async setAvailabilitySelect(availability: string): Promise<void> {
    await this.availabilitySelect.sendKeys(availability);
  }

  async getAvailabilitySelect(): Promise<string> {
    return await this.availabilitySelect.element(by.css('option:checked')).getText();
  }

  async availabilitySelectLastOption(): Promise<void> {
    await this.availabilitySelect.all(by.tagName('option')).last().click();
  }

  async setPriceInput(price: string): Promise<void> {
    await this.priceInput.sendKeys(price);
  }

  async getPriceInput(): Promise<string> {
    return await this.priceInput.getAttribute('value');
  }

  async setCategoryInput(category: string): Promise<void> {
    await this.categoryInput.sendKeys(category);
  }

  async getCategoryInput(): Promise<string> {
    return await this.categoryInput.getAttribute('value');
  }

  async companySelectLastOption(): Promise<void> {
    await this.companySelect.all(by.tagName('option')).last().click();
  }

  async companySelectOption(option: string): Promise<void> {
    await this.companySelect.sendKeys(option);
  }

  getCompanySelect(): ElementFinder {
    return this.companySelect;
  }

  async getCompanySelectedOption(): Promise<string> {
    return await this.companySelect.element(by.css('option:checked')).getText();
  }

  async coordinateSelectLastOption(): Promise<void> {
    await this.coordinateSelect.all(by.tagName('option')).last().click();
  }

  async coordinateSelectOption(option: string): Promise<void> {
    await this.coordinateSelect.sendKeys(option);
  }

  getCoordinateSelect(): ElementFinder {
    return this.coordinateSelect;
  }

  async getCoordinateSelectedOption(): Promise<string> {
    return await this.coordinateSelect.element(by.css('option:checked')).getText();
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

export class ProductDeleteDialog {
  private dialogTitle = element(by.id('stl-delete-product-heading'));
  private confirmButton = element(by.id('stl-confirm-delete-product'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('stlTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
