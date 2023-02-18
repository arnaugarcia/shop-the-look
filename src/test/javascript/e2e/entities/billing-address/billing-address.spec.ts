import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { BillingAddressComponentsPage, BillingAddressDeleteDialog, BillingAddressUpdatePage } from './billing-address.page-object';

const expect = chai.expect;

describe('BillingAddress e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let billingAddressComponentsPage: BillingAddressComponentsPage;
  let billingAddressUpdatePage: BillingAddressUpdatePage;
  let billingAddressDeleteDialog: BillingAddressDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load BillingAddresses', async () => {
    await navBarPage.goToEntity('billing-address');
    billingAddressComponentsPage = new BillingAddressComponentsPage();
    await browser.wait(ec.visibilityOf(billingAddressComponentsPage.title), 5000);
    expect(await billingAddressComponentsPage.getTitle()).to.eq('shopTheLookApp.billingAddress.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(billingAddressComponentsPage.entities), ec.visibilityOf(billingAddressComponentsPage.noResult)),
      1000
    );
  });

  it('should load create BillingAddress page', async () => {
    await billingAddressComponentsPage.clickOnCreateButton();
    billingAddressUpdatePage = new BillingAddressUpdatePage();
    expect(await billingAddressUpdatePage.getPageTitle()).to.eq('shopTheLookApp.billingAddress.home.createOrEditLabel');
    await billingAddressUpdatePage.cancel();
  });

  it('should create and save BillingAddresses', async () => {
    const nbButtonsBeforeCreate = await billingAddressComponentsPage.countDeleteButtons();

    await billingAddressComponentsPage.clickOnCreateButton();

    await promise.all([
      billingAddressUpdatePage.setAddressInput('address'),
      billingAddressUpdatePage.setCityInput('city'),
      billingAddressUpdatePage.setProvinceInput('province'),
      billingAddressUpdatePage.setZipCodeInput('zipCode'),
      billingAddressUpdatePage.setCountryInput('country'),
    ]);

    await billingAddressUpdatePage.save();
    expect(await billingAddressUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await billingAddressComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last BillingAddress', async () => {
    const nbButtonsBeforeDelete = await billingAddressComponentsPage.countDeleteButtons();
    await billingAddressComponentsPage.clickOnLastDeleteButton();

    billingAddressDeleteDialog = new BillingAddressDeleteDialog();
    expect(await billingAddressDeleteDialog.getDialogTitle()).to.eq('shopTheLookApp.billingAddress.delete.question');
    await billingAddressDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(billingAddressComponentsPage.title), 5000);

    expect(await billingAddressComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
