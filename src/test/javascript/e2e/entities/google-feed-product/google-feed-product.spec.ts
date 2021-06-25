import { browser, ExpectedConditions as ec /* , protractor, promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  GoogleFeedProductComponentsPage,
  /* GoogleFeedProductDeleteDialog, */
  GoogleFeedProductUpdatePage,
} from './google-feed-product.page-object';

const expect = chai.expect;

describe('GoogleFeedProduct e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let googleFeedProductComponentsPage: GoogleFeedProductComponentsPage;
  let googleFeedProductUpdatePage: GoogleFeedProductUpdatePage;
  /* let googleFeedProductDeleteDialog: GoogleFeedProductDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load GoogleFeedProducts', async () => {
    await navBarPage.goToEntity('google-feed-product');
    googleFeedProductComponentsPage = new GoogleFeedProductComponentsPage();
    await browser.wait(ec.visibilityOf(googleFeedProductComponentsPage.title), 5000);
    expect(await googleFeedProductComponentsPage.getTitle()).to.eq('stlApp.googleFeedProduct.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(googleFeedProductComponentsPage.entities), ec.visibilityOf(googleFeedProductComponentsPage.noResult)),
      1000
    );
  });

  it('should load create GoogleFeedProduct page', async () => {
    await googleFeedProductComponentsPage.clickOnCreateButton();
    googleFeedProductUpdatePage = new GoogleFeedProductUpdatePage();
    expect(await googleFeedProductUpdatePage.getPageTitle()).to.eq('stlApp.googleFeedProduct.home.createOrEditLabel');
    await googleFeedProductUpdatePage.cancel();
  });

  /* it('should create and save GoogleFeedProducts', async () => {
        const nbButtonsBeforeCreate = await googleFeedProductComponentsPage.countDeleteButtons();

        await googleFeedProductComponentsPage.clickOnCreateButton();

        await promise.all([
            googleFeedProductUpdatePage.setSkuInput('sku'),
            googleFeedProductUpdatePage.setNameInput('name'),
            googleFeedProductUpdatePage.setDescriptionInput('description'),
            googleFeedProductUpdatePage.setLinkInput('link'),
            googleFeedProductUpdatePage.setImageLinkInput('imageLink'),
            googleFeedProductUpdatePage.setAditionalImageLinkInput('aditionalImageLink'),
            googleFeedProductUpdatePage.setMobileLinkInput('mobileLink'),
            googleFeedProductUpdatePage.availabilitySelectLastOption(),
            googleFeedProductUpdatePage.setAvailabilityDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
            googleFeedProductUpdatePage.setPriceInput('price'),
            googleFeedProductUpdatePage.setSalePriceInput('salePrice'),
            googleFeedProductUpdatePage.setBrandInput('brand'),
            googleFeedProductUpdatePage.conditionSelectLastOption(),
            googleFeedProductUpdatePage.getAdultInput().click(),
            googleFeedProductUpdatePage.ageGroupSelectLastOption(),
            googleFeedProductUpdatePage.companySelectLastOption(),
        ]);

        await googleFeedProductUpdatePage.save();
        expect(await googleFeedProductUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await googleFeedProductComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last GoogleFeedProduct', async () => {
        const nbButtonsBeforeDelete = await googleFeedProductComponentsPage.countDeleteButtons();
        await googleFeedProductComponentsPage.clickOnLastDeleteButton();

        googleFeedProductDeleteDialog = new GoogleFeedProductDeleteDialog();
        expect(await googleFeedProductDeleteDialog.getDialogTitle())
            .to.eq('stlApp.googleFeedProduct.delete.question');
        await googleFeedProductDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(googleFeedProductComponentsPage.title), 5000);

        expect(await googleFeedProductComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
