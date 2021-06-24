import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { SubscriptionPlanComponentsPage, SubscriptionPlanDeleteDialog, SubscriptionPlanUpdatePage } from './subscription-plan.page-object';

const expect = chai.expect;

describe('SubscriptionPlan e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let subscriptionPlanComponentsPage: SubscriptionPlanComponentsPage;
  let subscriptionPlanUpdatePage: SubscriptionPlanUpdatePage;
  let subscriptionPlanDeleteDialog: SubscriptionPlanDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load SubscriptionPlans', async () => {
    await navBarPage.goToEntity('subscription-plan');
    subscriptionPlanComponentsPage = new SubscriptionPlanComponentsPage();
    await browser.wait(ec.visibilityOf(subscriptionPlanComponentsPage.title), 5000);
    expect(await subscriptionPlanComponentsPage.getTitle()).to.eq('stlApp.subscriptionPlan.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(subscriptionPlanComponentsPage.entities), ec.visibilityOf(subscriptionPlanComponentsPage.noResult)),
      1000
    );
  });

  it('should load create SubscriptionPlan page', async () => {
    await subscriptionPlanComponentsPage.clickOnCreateButton();
    subscriptionPlanUpdatePage = new SubscriptionPlanUpdatePage();
    expect(await subscriptionPlanUpdatePage.getPageTitle()).to.eq('stlApp.subscriptionPlan.home.createOrEditLabel');
    await subscriptionPlanUpdatePage.cancel();
  });

  it('should create and save SubscriptionPlans', async () => {
    const nbButtonsBeforeCreate = await subscriptionPlanComponentsPage.countDeleteButtons();

    await subscriptionPlanComponentsPage.clickOnCreateButton();

    await promise.all([
      subscriptionPlanUpdatePage.setNameInput('name'),
      subscriptionPlanUpdatePage.setDescriptionInput('description'),
      subscriptionPlanUpdatePage.categorySelectLastOption(),
      subscriptionPlanUpdatePage.setMaxProductsInput('5'),
      subscriptionPlanUpdatePage.setMaxSpacesInput('5'),
      subscriptionPlanUpdatePage.setMaxRequestsInput('5'),
    ]);

    await subscriptionPlanUpdatePage.save();
    expect(await subscriptionPlanUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await subscriptionPlanComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last SubscriptionPlan', async () => {
    const nbButtonsBeforeDelete = await subscriptionPlanComponentsPage.countDeleteButtons();
    await subscriptionPlanComponentsPage.clickOnLastDeleteButton();

    subscriptionPlanDeleteDialog = new SubscriptionPlanDeleteDialog();
    expect(await subscriptionPlanDeleteDialog.getDialogTitle()).to.eq('stlApp.subscriptionPlan.delete.question');
    await subscriptionPlanDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(subscriptionPlanComponentsPage.title), 5000);

    expect(await subscriptionPlanComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
