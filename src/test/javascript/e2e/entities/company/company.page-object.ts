import { element, by, ElementFinder } from 'protractor';

export class CompanyComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('stl-company div table .btn-danger'));
  title = element.all(by.css('stl-company div h2#page-heading span')).first();
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

export class CompanyUpdatePage {
  pageTitle = element(by.id('stl-company-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nameInput = element(by.id('field_name'));
  commercialNameInput = element(by.id('field_commercialName'));
  nifInput = element(by.id('field_nif'));
  logoInput = element(by.id('field_logo'));
  vatInput = element(by.id('field_vat'));
  urlInput = element(by.id('field_url'));
  phoneInput = element(by.id('field_phone'));
  emailInput = element(by.id('field_email'));
  typeSelect = element(by.id('field_type'));
  tokenInput = element(by.id('field_token'));
  referenceInput = element(by.id('field_reference'));
  industrySelect = element(by.id('field_industry'));
  companySizeSelect = element(by.id('field_companySize'));

  billingAddressSelect = element(by.id('field_billingAddress'));
  userSelect = element(by.id('field_user'));
  subscriptionPlanSelect = element(by.id('field_subscriptionPlan'));

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

  async setCommercialNameInput(commercialName: string): Promise<void> {
    await this.commercialNameInput.sendKeys(commercialName);
  }

  async getCommercialNameInput(): Promise<string> {
    return await this.commercialNameInput.getAttribute('value');
  }

  async setNifInput(nif: string): Promise<void> {
    await this.nifInput.sendKeys(nif);
  }

  async getNifInput(): Promise<string> {
    return await this.nifInput.getAttribute('value');
  }

  async setLogoInput(logo: string): Promise<void> {
    await this.logoInput.sendKeys(logo);
  }

  async getLogoInput(): Promise<string> {
    return await this.logoInput.getAttribute('value');
  }

  async setVatInput(vat: string): Promise<void> {
    await this.vatInput.sendKeys(vat);
  }

  async getVatInput(): Promise<string> {
    return await this.vatInput.getAttribute('value');
  }

  async setUrlInput(url: string): Promise<void> {
    await this.urlInput.sendKeys(url);
  }

  async getUrlInput(): Promise<string> {
    return await this.urlInput.getAttribute('value');
  }

  async setPhoneInput(phone: string): Promise<void> {
    await this.phoneInput.sendKeys(phone);
  }

  async getPhoneInput(): Promise<string> {
    return await this.phoneInput.getAttribute('value');
  }

  async setEmailInput(email: string): Promise<void> {
    await this.emailInput.sendKeys(email);
  }

  async getEmailInput(): Promise<string> {
    return await this.emailInput.getAttribute('value');
  }

  async setTypeSelect(type: string): Promise<void> {
    await this.typeSelect.sendKeys(type);
  }

  async getTypeSelect(): Promise<string> {
    return await this.typeSelect.element(by.css('option:checked')).getText();
  }

  async typeSelectLastOption(): Promise<void> {
    await this.typeSelect.all(by.tagName('option')).last().click();
  }

  async setTokenInput(token: string): Promise<void> {
    await this.tokenInput.sendKeys(token);
  }

  async getTokenInput(): Promise<string> {
    return await this.tokenInput.getAttribute('value');
  }

  async setReferenceInput(reference: string): Promise<void> {
    await this.referenceInput.sendKeys(reference);
  }

  async getReferenceInput(): Promise<string> {
    return await this.referenceInput.getAttribute('value');
  }

  async setIndustrySelect(industry: string): Promise<void> {
    await this.industrySelect.sendKeys(industry);
  }

  async getIndustrySelect(): Promise<string> {
    return await this.industrySelect.element(by.css('option:checked')).getText();
  }

  async industrySelectLastOption(): Promise<void> {
    await this.industrySelect.all(by.tagName('option')).last().click();
  }

  async setCompanySizeSelect(companySize: string): Promise<void> {
    await this.companySizeSelect.sendKeys(companySize);
  }

  async getCompanySizeSelect(): Promise<string> {
    return await this.companySizeSelect.element(by.css('option:checked')).getText();
  }

  async companySizeSelectLastOption(): Promise<void> {
    await this.companySizeSelect.all(by.tagName('option')).last().click();
  }

  async billingAddressSelectLastOption(): Promise<void> {
    await this.billingAddressSelect.all(by.tagName('option')).last().click();
  }

  async billingAddressSelectOption(option: string): Promise<void> {
    await this.billingAddressSelect.sendKeys(option);
  }

  getBillingAddressSelect(): ElementFinder {
    return this.billingAddressSelect;
  }

  async getBillingAddressSelectedOption(): Promise<string> {
    return await this.billingAddressSelect.element(by.css('option:checked')).getText();
  }

  async userSelectLastOption(): Promise<void> {
    await this.userSelect.all(by.tagName('option')).last().click();
  }

  async userSelectOption(option: string): Promise<void> {
    await this.userSelect.sendKeys(option);
  }

  getUserSelect(): ElementFinder {
    return this.userSelect;
  }

  async getUserSelectedOption(): Promise<string> {
    return await this.userSelect.element(by.css('option:checked')).getText();
  }

  async subscriptionPlanSelectLastOption(): Promise<void> {
    await this.subscriptionPlanSelect.all(by.tagName('option')).last().click();
  }

  async subscriptionPlanSelectOption(option: string): Promise<void> {
    await this.subscriptionPlanSelect.sendKeys(option);
  }

  getSubscriptionPlanSelect(): ElementFinder {
    return this.subscriptionPlanSelect;
  }

  async getSubscriptionPlanSelectedOption(): Promise<string> {
    return await this.subscriptionPlanSelect.element(by.css('option:checked')).getText();
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

export class CompanyDeleteDialog {
  private dialogTitle = element(by.id('stl-delete-company-heading'));
  private confirmButton = element(by.id('stl-confirm-delete-company'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('stlTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
