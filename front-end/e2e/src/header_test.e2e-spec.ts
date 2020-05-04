import {browser, logging, by, element, protractor} from 'protractor';

describe('test header', () => {

  beforeAll(() => {
    browser.get(browser.baseUrl);
  });

  it('should display the good title', () => {
    const nabar = element(by.css('.navbar-brand')).getText() as Promise<string>;
    expect(nabar).toEqual('CoronaMap');
  });

  it('should display the good img', () => {
    const navbar = element(by.css('.navbar-brand'));
    const img = navbar.element(by.tagName('img'));
    img.isPresent();
    const imgSrc = img.getAttribute('src');
    expect(imgSrc).toEqual('http://localhost:4200/assets/images/virus.svg');
  });

  it('should display the good number of menu options', () => {
    const navbar = element(by.tagName('app-nav-bar'));
    const navbarmenu = navbar.element(by.tagName('ul'));
    const liste = navbarmenu.all(by.tagName('li'));
    expect(liste.count()).toEqual(2);
  });

  it('should display the good name of menu options', () => {
    const navbar = element(by.tagName('app-nav-bar'));
    const navbarmenu = navbar.element(by.tagName('ul'));
    const liste = navbarmenu.all(by.tagName('li'));
    expect(liste.first().getText()).toEqual('Map');
    expect(liste.last().getText()).toEqual('Simulation');
  });

  it('should display the good page on click on Map', () => {
    const navbar = element(by.tagName('app-nav-bar'));
    const navbarmenu = navbar.element(by.tagName('ul'));
    const liste = navbarmenu.all(by.tagName('li'));
    liste.first().click();
    // Test URL contains map
    const EC = protractor.ExpectedConditions;
    browser.wait(EC.urlContains('map'), 5000);
  });

  it('should display the good page on click on Simulation', () => {
    const navbar = element(by.tagName('app-nav-bar'));
    const navbarmenu = navbar.element(by.tagName('ul'));
    const liste = navbarmenu.all(by.tagName('li'));
    liste.last().click();
    // Test URL contains map
    const EC = protractor.ExpectedConditions;
    browser.wait(EC.urlContains('simulation'), 5000);
  });

  afterEach(async () => {
    // Assert that there are no errors emitted from the browser
    const logs = await browser.manage().logs().get(logging.Type.BROWSER);
    expect(logs).not.toContain(jasmine.objectContaining({
      level: logging.Level.SEVERE,
    } as logging.Entry));
  });
});
