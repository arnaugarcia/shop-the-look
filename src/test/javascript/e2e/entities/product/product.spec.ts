import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  ProductComponentsPage,
  /* ProductDeleteDialog, */
  ProductUpdatePage,
} from './product.page-object';

const expect = chai.expect;

describe('Product e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let productComponentsPage: ProductComponentsPage;
  let productUpdatePage: ProductUpdatePage;
  /* let productDeleteDialog: ProductDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Products', async () => {
    await navBarPage.goToEntity('product');
    productComponentsPage = new ProductComponentsPage();
    await browser.wait(ec.visibilityOf(productComponentsPage.title), 5000);
    expect(await productComponentsPage.getTitle()).to.eq('shopTheLookApp.product.home.title');
    await browser.wait(ec.or(ec.visibilityOf(productComponentsPage.entities), ec.visibilityOf(productComponentsPage.noResult)), 1000);
  });

  it('should load create Product page', async () => {
    await productComponentsPage.clickOnCreateButton();
    productUpdatePage = new ProductUpdatePage();
    expect(await productUpdatePage.getPageTitle()).to.eq('shopTheLookApp.product.home.createOrEditLabel');
    await productUpdatePage.cancel();
  });

  /* it('should create and save Products', async () => {
        const nbButtonsBeforeCreate = await productComponentsPage.countDeleteButtons();

        await productComponentsPage.clickOnCreateButton();

        await promise.all([
            productUpdatePage.setSkuInput('sku'),
            productUpdatePage.setNameInput('name'),
            productUpdatePage.setDescriptionInput('description'),
            productUpdatePage.setLinkInput('link'),
            productUpdatePage.setImageLinkInput('imageLink'),
            productUpdatePage.setAdditionalImageLinkInput('additionalImageLink'),
            productUpdatePage.availabilitySelectLastOption(),
            productUpdatePage.setPriceInput('price'),
            productUpdatePage.setCategoryInput('category'),
            productUpdatePage.companySelectLastOption(),
            productUpdatePage.coordinateSelectLastOption(),
        ]);

        await productUpdatePage.save();
        expect(await productUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await productComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last Product', async () => {
        const nbButtonsBeforeDelete = await productComponentsPage.countDeleteButtons();
        await productComponentsPage.clickOnLastDeleteButton();

        productDeleteDialog = new ProductDeleteDialog();
        expect(await productDeleteDialog.getDialogTitle())
            .to.eq('shopTheLookApp.product.delete.question');
        await productDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(productComponentsPage.title), 5000);

        expect(await productComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
