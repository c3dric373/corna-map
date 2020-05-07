import {browser, logging, by, element, protractor} from 'protractor';
import {applySourceSpanToExpressionIfNeeded} from '@angular/compiler/src/output/output_ast';

describe('test map content', () => {

  beforeAll(() => {
    browser.get(browser.baseUrl);
  });

  it('should contain button Régions', () => {
    const mapComponent = element(by.tagName('app-map-content'));
    const nav = mapComponent.element(by.tagName('nav'));
    const buttons = nav.all(by.tagName('button'));
    const regButton = buttons.first();
    expect(regButton.getText()).toEqual('Régions');
  });

  it('should contain button Départements', () => {
    const mapComponent = element(by.tagName('app-map-content'));
    const nav = mapComponent.element(by.tagName('nav'));
    const buttons = nav.all(by.tagName('button'));
    const deptButton = buttons.get(1);
    expect(deptButton.getText()).toEqual('Départements');
  });

  it('should contain drop-down button Cas confirmés', () => {
    const mapComponent = element(by.tagName('app-map-content'));
    const nav = mapComponent.element(by.tagName('nav'));
    const dropdown = nav.all(by.css('.dropdown-toggle'));
    expect(dropdown.first().getText()).toEqual('Cas confirmés');
  });

  it('should contain drop-down button Cas confirmés hidden options', () => {
    const mapComponent = element(by.tagName('app-map-content'));
    const nav = mapComponent.element(by.tagName('nav'));
    const dropdownMenu = nav.all(by.css('.dropdown-menu')).get(0);
    expect(dropdownMenu.isDisplayed()).toBe(false);
  });

  it('should contain drop-down button Cas confirmés view options', () => {
    const mapComponent = element(by.tagName('app-map-content'));
    const nav = mapComponent.element(by.tagName('nav'));
    nav.all(by.css('.dropdown-toggle')).get(0).click();
    const dropdownMenu = nav.all(by.css('.dropdown-menu')).get(0);
    expect(dropdownMenu.isDisplayed()).toBe(true);
  });

  it('should contain drop-down button date', () => {
    const mapComponent = element(by.tagName('app-map-content'));
    const nav = mapComponent.element(by.tagName('nav'));
    const dropdown = nav.all(by.css('.dropdown-toggle'));
    expect(dropdown.get(1).getText()).toContain('/2020');
  });

  it('should contain drop-down button date hidden options', () => {
    const mapComponent = element(by.tagName('app-map-content'));
    const nav = mapComponent.element(by.tagName('nav'));
    const dropdownMenu = nav.all(by.css('.dropdown-menu')).get(1);
    expect(dropdownMenu.isDisplayed()).toBe(false);
  });

  it('should contain drop-down button date view options', () => {
    const mapComponent = element(by.tagName('app-map-content'));
    const nav = mapComponent.element(by.tagName('nav'));
    nav.all(by.css('.dropdown-toggle')).get(1).click();
    const dropdownMenu = nav.all(by.css('.dropdown-menu')).get(1);
    expect(dropdownMenu.isDisplayed()).toBe(true);
    nav.all(by.css('.dropdown-toggle')).get(1).click();
  });

  it('should extand map', () => {
    const mapComponent = element(by.tagName('app-map-content'));
    const nav = mapComponent.element(by.tagName('nav'));
    const button = nav.all(by.tagName('button')).last();
    button.element(by.tagName('svg')).element(by.className('a-expand-alt')).isPresent();
    button.click();
    const rightBar = element(by.tagName('app-right-bar'));
    expect(rightBar.isDisplayed()).toBe(false);
  });

  it('should compress map', () => {
    const mapComponent = element(by.tagName('app-map-content'));
    const nav = mapComponent.element(by.tagName('nav'));
    const button = nav.all(by.tagName('button')).last();
    button.element(by.tagName('svg')).element(by.className('a-compress-alt')).isPresent();
    button.click();
    const rightBar = element(by.tagName('app-right-bar'));
    expect(rightBar.isDisplayed()).toBe(true);
  });

  it('should display the map', () => {
    const mapComponent = element(by.tagName('app-map-content'));
    const carte = mapComponent.element(by.className('carte'));
    expect(carte.isDisplayed()).toBe(true);
  });

  afterEach(async () => {
  // Assert that there are no errors emitted from the browser
  const logs = await browser.manage().logs().get(logging.Type.BROWSER);
  expect(logs).not.toContain(jasmine.objectContaining({
    level: logging.Level.SEVERE,
  } as logging.Entry));
});

});
