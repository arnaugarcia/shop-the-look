import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  SpaceTemplateComponentsPage,
  /* SpaceTemplateDeleteDialog, */
  SpaceTemplateUpdatePage,
} from './space-template.page-object';

const expect = chai.expect;

describe('SpaceTemplate e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let spaceTemplateComponentsPage: SpaceTemplateComponentsPage;
  let spaceTemplateUpdatePage: SpaceTemplateUpdatePage;
  /* let spaceTemplateDeleteDialog: SpaceTemplateDeleteDialog; */
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SpaceTemplates', async () => {
    await navBarPage.goToEntity('space-template');
    spaceTemplateComponentsPage = new SpaceTemplateComponentsPage();
    await browser.wait(ec.visibilityOf(spaceTemplateComponentsPage.title), 5000);
    expect(await spaceTemplateComponentsPage.getTitle()).to.eq('stlApp.spaceTemplate.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(spaceTemplateComponentsPage.entities), ec.visibilityOf(spaceTemplateComponentsPage.noResult)),
      1000
    );
  });

  it('should load create SpaceTemplate page', async () => {
    await spaceTemplateComponentsPage.clickOnCreateButton();
    spaceTemplateUpdatePage = new SpaceTemplateUpdatePage();
    expect(await spaceTemplateUpdatePage.getPageTitle()).to.eq('stlApp.spaceTemplate.home.createOrEditLabel');
    await spaceTemplateUpdatePage.cancel();
  });

  /* it('should create and save SpaceTemplates', async () => {
        const nbButtonsBeforeCreate = await spaceTemplateComponentsPage.countDeleteButtons();

        await spaceTemplateComponentsPage.clickOnCreateButton();

        await promise.all([
            spaceTemplateUpdatePage.setNameInput('name'),
            spaceTemplateUpdatePage.setDescriptionInput('description'),
            spaceTemplateUpdatePage.setMaxProductsInput('5'),
            spaceTemplateUpdatePage.setMaxPhotosInput('5'),
            spaceTemplateUpdatePage.getActiveInput().click(),
        ]);

        await spaceTemplateUpdatePage.save();
        expect(await spaceTemplateUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await spaceTemplateComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /* it('should delete last SpaceTemplate', async () => {
        const nbButtonsBeforeDelete = await spaceTemplateComponentsPage.countDeleteButtons();
        await spaceTemplateComponentsPage.clickOnLastDeleteButton();

        spaceTemplateDeleteDialog = new SpaceTemplateDeleteDialog();
        expect(await spaceTemplateDeleteDialog.getDialogTitle())
            .to.eq('stlApp.spaceTemplate.delete.question');
        await spaceTemplateDeleteDialog.clickOnConfirmButton();
        await browser.wait(ec.visibilityOf(spaceTemplateComponentsPage.title), 5000);

        expect(await spaceTemplateComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
