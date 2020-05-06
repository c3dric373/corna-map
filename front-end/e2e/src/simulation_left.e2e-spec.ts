import {browser, logging, by, element, protractor} from 'protractor';

describe('test left simulation', () => {

  beforeAll(() => {
    browser.get(browser.baseUrl + '/simulation');
  });

  it('should contain Simulation', () => {
    const leftPanel = element(by.tagName('app-left-simulation'));
    const titre = leftPanel.element(by.tagName('h2'));
    expect(titre.getText()).toContain('Simulation');
  });

  it('should contain start Date', () => {
    const leftPanel = element(by.tagName('app-left-simulation'));
    const startDate = leftPanel.all(by.className('date')).get(0);
    expect(startDate.getText()).toContain('Date de début : ');
    const calendar = startDate.all(by.tagName('button')).get(0);
    expect(calendar.getText()).toContain('/2020');
  });

  it('should contain end Date', () => {
    const leftPanel = element(by.tagName('app-left-simulation'));
    const startDate = leftPanel.all(by.className('date')).get(1);
    expect(startDate.getText()).toContain('Date de fin : ');
    const calendar = startDate.all(by.tagName('button')).get(0);
    expect(calendar.getText()).toContain('/2020');
  });

  it('should contain Play Button', () => {
    const leftPanel = element(by.tagName('app-left-simulation'));
    const buttonsGroup = leftPanel.element(by.id('buttons1'));
    const playButton = buttonsGroup.element(by.id('play'));
    expect(playButton.isPresent()).toBe(true);
    const icon = playButton.element(by.className('fa-play'));
    expect(icon.isPresent()).toBe(true);
  });

  it('should contain Pause Button', () => {
    const leftPanel = element(by.tagName('app-left-simulation'));
    const buttonsGroup = leftPanel.element(by.id('buttons1'));
    const pauseButton = buttonsGroup.element(by.id('pause'));
    expect(pauseButton.isPresent()).toBe(true);
    const icon = pauseButton.element(by.className('fa-pause'));
    expect(icon.isPresent()).toBe(true);
  });

  it('should contain Stop Button', () => {
    const leftPanel = element(by.tagName('app-left-simulation'));
    const buttonsGroup = leftPanel.element(by.id('buttons1'));
    const stopButton = buttonsGroup.element(by.id('stop'));
    expect(stopButton.isPresent()).toBe(true);
    const icon = stopButton.element(by.className('fa-stop'));
    expect(icon.isPresent()).toBe(true);
  });

  it('should contain Accordion', () => {
    const leftPanel = element(by.tagName('app-left-simulation'));
    const Accordion = leftPanel.element(by.tagName('ngb-accordion'));
    expect(Accordion.isPresent()).toBe(true);
  });

  it('should contain Mesures du gouvernement', () => {
    const leftPanel = element(by.tagName('app-left-simulation'));
    const accordion = leftPanel.element(by.tagName('ngb-accordion'));
    const cards = accordion.all(by.className('card'));
    const button = cards.get(0).element(by.tagName('button'));
    expect(button.getText()).toEqual('Mesures du gouvernement' );
  });

  it('should contain right content onClick on Mesures du gouvernement', () => {
    const leftPanel = element(by.tagName('app-left-simulation'));
    const accordion = leftPanel.element(by.tagName('ngb-accordion'));
    const cards = accordion.all(by.className('card'));
    const button = cards.get(0).element(by.tagName('button'));
    button.click();
    const cardBody = cards.get(0).element(by.className('card-body'));
    const span = cardBody.all(by.tagName('span'));
    expect(span.get(0).getText()).toEqual('Confinement' );
    const confinementTab = cardBody.element(by.id('confinement'));
    const confinementButtons = confinementTab.all(by.tagName('button'));
    expect(confinementButtons.count()).toEqual(5);
    const maskTab = cardBody.element(by.id('masque'));
    const maskButtons = maskTab.all(by.tagName('button'));
    expect(maskButtons.count()).toEqual(5);
  });

  it('should contain Comportements', () => {
    const leftPanel = element(by.tagName('app-left-simulation'));
    const accordion = leftPanel.element(by.tagName('ngb-accordion'));
    const cards = accordion.all(by.className('card'));
    const button = cards.get(1).element(by.tagName('button'));
    expect(button.getText()).toEqual('Comportements' );
  });

  it('should contain right content onClick on Comportements', () => {
    const leftPanel = element(by.tagName('app-left-simulation'));
    const accordion = leftPanel.element(by.tagName('ngb-accordion'));
    const cards = accordion.all(by.className('card'));
    const button = cards.get(1).element(by.tagName('button'));
    button.click();
    const cardBody = cards.get(1).element(by.className('card-body'));
    const span = cardBody.all(by.tagName('span'));
    expect(span.get(0).getText()).toEqual('Respect du confinement' );
    const slider = cardBody.element(by.tagName('mat-slider'));
    expect(slider.isPresent()).toEqual(true);
  });

  it('should contain Ecoulement du temps de la simulation', () => {
    const leftPanel = element(by.tagName('app-left-simulation'));
    const accordion = leftPanel.element(by.tagName('ngb-accordion'));
    const cards = accordion.all(by.className('card'));
    const button = cards.get(2).element(by.tagName('button'));
    expect(button.getText()).toEqual('Ecoulement du temps de la simulation' );
  });

  it('should contain right content onClick on Ecoulement du temps de la simulation', () => {
    const leftPanel = element(by.tagName('app-left-simulation'));
    const accordion = leftPanel.element(by.tagName('ngb-accordion'));
    const cards = accordion.all(by.className('card'));
    const button = cards.get(2).element(by.tagName('button'));
    button.click();
    const cardBody = cards.get(2).element(by.className('card-body'));
    const span = cardBody.all(by.tagName('span'));
    expect(span.get(0).getText()).toEqual('Ecoulement des jours' );
    const slider = cardBody.element(by.tagName('mat-slider'));
    expect(slider.isPresent()).toEqual(true);
  });

  afterEach(async () => {
    // Assert that there are no errors emitted from the browser
    const logs = await browser.manage().logs().get(logging.Type.BROWSER);
    expect(logs).not.toContain(jasmine.objectContaining({
      level: logging.Level.SEVERE,
    } as logging.Entry));
  });

});
