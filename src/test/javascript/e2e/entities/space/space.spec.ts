import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  SpaceComponentsPage,
  /* SpaceDeleteDialog, */
  SpaceUpdatePage,
} from './space.page-object';

const expect = chai.expect;

describe('Space e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let spaceComponentsPage: SpaceComponentsPage;
  let spaceUpdatePage: SpaceUpdatePage;
  /* let spaceDeleteDialog: SpaceDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Spaces', async () => {
    await navBarPage.goToEntity('space');
    spaceComponentsPage = new SpaceComponentsPage();
    await browser.wait(ec.visibilityOf(spaceComponentsPage.title), 5000);
    expect(await spaceComponentsPage.getTitle()).to.eq('shopTheLookApp.space.home.title');
    await browser.wait(ec.or(ec.visibilityOf(spaceComponentsPage.entities), ec.visibilityOf(spaceComponentsPage.noResult)), 1000);
  });

  it('should load create Space page', async () => {
    await spaceComponentsPage.clickOnCreateButton();
    spaceUpdatePage = new SpaceUpdatePage();
    expect(await spaceUpdatePage.getPageTitle()).to.eq('shopTheLookApp.space.home.createOrEditLabel');
    await spaceUpdatePage.cancel();
  });

  /* it('should create and save Spaces', async () => {
        const nbButtonsBeforeCreate = await spaceComponentsPage.countDeleteButtons();

        await spaceComponentsPage.clickOnCreateButton();

        await promise.all([
            spaceUpdatePage.setNameInput('name'),
            spaceUpdatePage.getActiveInput().click(),
            spaceUpdatePage.setReferenceInput('reference'),
            spaceUpdatePage.setDescriptionInput('description'),
            spaceUpdatePage.setMaxPhotosInput('5'),
            spaceUpdatePage.getVisibleInput().click(),
            spaceUpdatePage.companySelectLastOption(),
        ]);

        await spaceUpdatePage.save();
        expect(await spaceUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await spaceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last Space', async () => {
        const nbButtonsBeforeDelete = await spaceComponentsPage.countDeleteButtons();
        await spaceComponentsPage.clickOnLastDeleteButton();

        spaceDeleteDialog = new SpaceDeleteDialog();
        expect(await spaceDeleteDialog.getDialogTitle())
            .to.eq('shopTheLookApp.space.delete.question');
        await spaceDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(spaceComponentsPage.title), 5000);

        expect(await spaceComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
