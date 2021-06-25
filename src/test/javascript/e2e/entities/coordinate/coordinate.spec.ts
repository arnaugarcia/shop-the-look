import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  CoordinateComponentsPage,
  /* CoordinateDeleteDialog, */
  CoordinateUpdatePage,
} from './coordinate.page-object';

const expect = chai.expect;

describe('Coordinate e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let coordinateComponentsPage: CoordinateComponentsPage;
  let coordinateUpdatePage: CoordinateUpdatePage;
  /* let coordinateDeleteDialog: CoordinateDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Coordinates', async () => {
    await navBarPage.goToEntity('coordinate');
    coordinateComponentsPage = new CoordinateComponentsPage();
    await browser.wait(ec.visibilityOf(coordinateComponentsPage.title), 5000);
    expect(await coordinateComponentsPage.getTitle()).to.eq('stlApp.coordinate.home.title');
    await browser.wait(ec.or(ec.visibilityOf(coordinateComponentsPage.entities), ec.visibilityOf(coordinateComponentsPage.noResult)), 1000);
  });

  it('should load create Coordinate page', async () => {
    await coordinateComponentsPage.clickOnCreateButton();
    coordinateUpdatePage = new CoordinateUpdatePage();
    expect(await coordinateUpdatePage.getPageTitle()).to.eq('stlApp.coordinate.home.createOrEditLabel');
    await coordinateUpdatePage.cancel();
  });

  /* it('should create and save Coordinates', async () => {
        const nbButtonsBeforeCreate = await coordinateComponentsPage.countDeleteButtons();

        await coordinateComponentsPage.clickOnCreateButton();

        await promise.all([
            coordinateUpdatePage.setXInput('5'),
            coordinateUpdatePage.setYInput('5'),
            coordinateUpdatePage.photoSelectLastOption(),
        ]);

        await coordinateUpdatePage.save();
        expect(await coordinateUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await coordinateComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last Coordinate', async () => {
        const nbButtonsBeforeDelete = await coordinateComponentsPage.countDeleteButtons();
        await coordinateComponentsPage.clickOnLastDeleteButton();

        coordinateDeleteDialog = new CoordinateDeleteDialog();
        expect(await coordinateDeleteDialog.getDialogTitle())
            .to.eq('stlApp.coordinate.delete.question');
        await coordinateDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(coordinateComponentsPage.title), 5000);

        expect(await coordinateComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
