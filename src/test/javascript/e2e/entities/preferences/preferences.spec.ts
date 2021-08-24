import { browser, ExpectedConditions as ec, promise, protractor } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { PreferencesComponentsPage, PreferencesDeleteDialog, PreferencesUpdatePage } from './preferences.page-object';

const expect = chai.expect;

describe('Preferences e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let preferencesComponentsPage: PreferencesComponentsPage;
  let preferencesUpdatePage: PreferencesUpdatePage;
  let preferencesDeleteDialog: PreferencesDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Preferences', async () => {
    await navBarPage.goToEntity('preferences');
    preferencesComponentsPage = new PreferencesComponentsPage();
    await browser.wait(ec.visibilityOf(preferencesComponentsPage.title), 5000);
    expect(await preferencesComponentsPage.getTitle()).to.eq('shopTheLookApp.preferences.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(preferencesComponentsPage.entities), ec.visibilityOf(preferencesComponentsPage.noResult)),
      1000
    );
  });

  it('should load create Preferences page', async () => {
    await preferencesComponentsPage.clickOnCreateButton();
    preferencesUpdatePage = new PreferencesUpdatePage();
    expect(await preferencesUpdatePage.getPageTitle()).to.eq('shopTheLookApp.preferences.home.createOrEditLabel');
    await preferencesUpdatePage.cancel();
  });

  it('should create and save Preferences', async () => {
    const nbButtonsBeforeCreate = await preferencesComponentsPage.countDeleteButtons();

    await preferencesComponentsPage.clickOnCreateButton();

    await promise.all([
      preferencesUpdatePage.importMethodSelectLastOption(),
      preferencesUpdatePage.setFeedUrlInput('feedUrl'),
      preferencesUpdatePage.setRemainingImportsInput('5'),
      preferencesUpdatePage.setLastImportByInput('lastImportBy'),
      preferencesUpdatePage.setLastImportTimestampInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
    ]);

    await preferencesUpdatePage.save();
    expect(await preferencesUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await preferencesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Preferences', async () => {
    const nbButtonsBeforeDelete = await preferencesComponentsPage.countDeleteButtons();
    await preferencesComponentsPage.clickOnLastDeleteButton();

    preferencesDeleteDialog = new PreferencesDeleteDialog();
    expect(await preferencesDeleteDialog.getDialogTitle()).to.eq('shopTheLookApp.preferences.delete.question');
    await preferencesDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(preferencesComponentsPage.title), 5000);

    expect(await preferencesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
