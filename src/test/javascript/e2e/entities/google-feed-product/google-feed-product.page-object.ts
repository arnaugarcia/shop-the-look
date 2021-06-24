import { element, by, ElementFinder } from 'protractor';

export class GoogleFeedProductComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('stl-google-feed-product div table .btn-danger'));
  title = element.all(by.css('stl-google-feed-product div h2#page-heading span')).first();
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

export class GoogleFeedProductUpdatePage {
  pageTitle = element(by.id('stl-google-feed-product-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  skuInput = element(by.id('field_sku'));
  nameInput = element(by.id('field_name'));
  descriptionInput = element(by.id('field_description'));
  linkInput = element(by.id('field_link'));
  imageLinkInput = element(by.id('field_imageLink'));
  aditionalImageLinkInput = element(by.id('field_aditionalImageLink'));
  mobileLinkInput = element(by.id('field_mobileLink'));
  availabilitySelect = element(by.id('field_availability'));
  availabilityDateInput = element(by.id('field_availabilityDate'));
  priceInput = element(by.id('field_price'));
  salePriceInput = element(by.id('field_salePrice'));
  brandInput = element(by.id('field_brand'));
  conditionSelect = element(by.id('field_condition'));
  adultInput = element(by.id('field_adult'));
  ageGroupSelect = element(by.id('field_ageGroup'));

  companySelect = element(by.id('field_company'));

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

  async setAditionalImageLinkInput(aditionalImageLink: string): Promise<void> {
    await this.aditionalImageLinkInput.sendKeys(aditionalImageLink);
  }

  async getAditionalImageLinkInput(): Promise<string> {
    return await this.aditionalImageLinkInput.getAttribute('value');
  }

  async setMobileLinkInput(mobileLink: string): Promise<void> {
    await this.mobileLinkInput.sendKeys(mobileLink);
  }

  async getMobileLinkInput(): Promise<string> {
    return await this.mobileLinkInput.getAttribute('value');
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

  async setAvailabilityDateInput(availabilityDate: string): Promise<void> {
    await this.availabilityDateInput.sendKeys(availabilityDate);
  }

  async getAvailabilityDateInput(): Promise<string> {
    return await this.availabilityDateInput.getAttribute('value');
  }

  async setPriceInput(price: string): Promise<void> {
    await this.priceInput.sendKeys(price);
  }

  async getPriceInput(): Promise<string> {
    return await this.priceInput.getAttribute('value');
  }

  async setSalePriceInput(salePrice: string): Promise<void> {
    await this.salePriceInput.sendKeys(salePrice);
  }

  async getSalePriceInput(): Promise<string> {
    return await this.salePriceInput.getAttribute('value');
  }

  async setBrandInput(brand: string): Promise<void> {
    await this.brandInput.sendKeys(brand);
  }

  async getBrandInput(): Promise<string> {
    return await this.brandInput.getAttribute('value');
  }

  async setConditionSelect(condition: string): Promise<void> {
    await this.conditionSelect.sendKeys(condition);
  }

  async getConditionSelect(): Promise<string> {
    return await this.conditionSelect.element(by.css('option:checked')).getText();
  }

  async conditionSelectLastOption(): Promise<void> {
    await this.conditionSelect.all(by.tagName('option')).last().click();
  }

  getAdultInput(): ElementFinder {
    return this.adultInput;
  }

  async setAgeGroupSelect(ageGroup: string): Promise<void> {
    await this.ageGroupSelect.sendKeys(ageGroup);
  }

  async getAgeGroupSelect(): Promise<string> {
    return await this.ageGroupSelect.element(by.css('option:checked')).getText();
  }

  async ageGroupSelectLastOption(): Promise<void> {
    await this.ageGroupSelect.all(by.tagName('option')).last().click();
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

export class GoogleFeedProductDeleteDialog {
  private dialogTitle = element(by.id('stl-delete-googleFeedProduct-heading'));
  private confirmButton = element(by.id('stl-confirm-delete-googleFeedProduct'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('stlTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
