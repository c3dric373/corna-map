import {browser, logging, by, element, protractor} from 'protractor';

describe('test header', () => {

  beforeAll(() => {
    browser.get(browser.baseUrl);
  });

  it('should contain France', () => {
    const sidebar = element(by.tagName('app-side-bar'));
    const titre = sidebar.element(by.id('titre'));
    expect(titre.getText()).toEqual('France');
  });

  it('should contain confirmed cases', () => {
    const sidebar = element(by.tagName('app-side-bar'));
    const cas = sidebar.element(by.id('CasConfirmes'));
    expect(cas.getText()).toContain('Cas confirmés');
  });

  it('should contain Guris cases', () => {
    const sidebar = element(by.tagName('app-side-bar'));
    const cas = sidebar.element(by.id('Guris'));
    expect(cas.getText()).toContain('Guéris');
  });

  it('should contain confirmed cases', () => {
    const sidebar = element(by.tagName('app-side-bar'));
    const cas = sidebar.element(by.id('Hospitalisations'));
    expect(cas.getText()).toContain('Hospitalisations');
  });

  it('should contain confirmed cases', () => {
    const sidebar = element(by.tagName('app-side-bar'));
    const cas = sidebar.element(by.id('Deces'));
    expect(cas.getText()).toContain('Décès');
  });

  /*afterEach(async () => {
   // Assert that there are no errors emitted from the browser
   const logs = await browser.manage().logs().get(logging.Type.BROWSER);
   expect(logs).not.toContain(jasmine.objectContaining({
     level: logging.Level.SEVERE,
   } as logging.Entry));
 });*/
});
