import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  CompanyComponentsPage,
  /* CompanyDeleteDialog, */
  CompanyUpdatePage,
} from './company.page-object';

const expect = chai.expect;

describe('Company e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let companyComponentsPage: CompanyComponentsPage;
  let companyUpdatePage: CompanyUpdatePage;
  /* let companyDeleteDialog: CompanyDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Companies', async () => {
    await navBarPage.goToEntity('company');
    companyComponentsPage = new CompanyComponentsPage();
    await browser.wait(ec.visibilityOf(companyComponentsPage.title), 5000);
    expect(await companyComponentsPage.getTitle()).to.eq('shopTheLookApp.company.home.title');
    await browser.wait(ec.or(ec.visibilityOf(companyComponentsPage.entities), ec.visibilityOf(companyComponentsPage.noResult)), 1000);
  });

  it('should load create Company page', async () => {
    await companyComponentsPage.clickOnCreateButton();
    companyUpdatePage = new CompanyUpdatePage();
    expect(await companyUpdatePage.getPageTitle()).to.eq('shopTheLookApp.company.home.createOrEditLabel');
    await companyUpdatePage.cancel();
  });

  /* it('should create and save Companies', async () => {
        const nbButtonsBeforeCreate = await companyComponentsPage.countDeleteButtons();

        await companyComponentsPage.clickOnCreateButton();

        await promise.all([
            companyUpdatePage.setNameInput('name'),
            companyUpdatePage.setCommercialNameInput('commercialName'),
            companyUpdatePage.setNifInput('nif'),
            companyUpdatePage.setLogoInput('logo'),
            companyUpdatePage.setVatInput('vat'),
            companyUpdatePage.setUrlInput('url'),
            companyUpdatePage.setPhoneInput('phone'),
            companyUpdatePage.setEmailInput('email'),
            companyUpdatePage.typeSelectLastOption(),
            companyUpdatePage.setTokenInput('token'),
            companyUpdatePage.setReferenceInput('reference'),
            companyUpdatePage.industrySelectLastOption(),
            companyUpdatePage.companySizeSelectLastOption(),
            companyUpdatePage.billingAddressSelectLastOption(),
            // companyUpdatePage.userSelectLastOption(),
            companyUpdatePage.subscriptionPlanSelectLastOption(),
        ]);

        await companyUpdatePage.save();
        expect(await companyUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await companyComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last Company', async () => {
        const nbButtonsBeforeDelete = await companyComponentsPage.countDeleteButtons();
        await companyComponentsPage.clickOnLastDeleteButton();

        companyDeleteDialog = new CompanyDeleteDialog();
        expect(await companyDeleteDialog.getDialogTitle())
            .to.eq('shopTheLookApp.company.delete.question');
        await companyDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(companyComponentsPage.title), 5000);

        expect(await companyComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
