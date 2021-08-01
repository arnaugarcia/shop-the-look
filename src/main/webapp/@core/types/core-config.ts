// prettier-ignore

export interface CoreConfig {
  layout: {
    skin: 'default' | 'dark';
    menu: {
      hidden: boolean;
      collapsed: boolean;
    };
    navbar: {
      hidden: boolean;
    };
    footer: {
      hidden: boolean;
    };
    scrollTop: boolean;
    enableLocalStorage: boolean;
  };
}

export class CoreConfig implements CoreConfig {
  constructor() {
    this.layout = {
      skin: 'default',
      menu: {
        hidden: false,
        collapsed: false,
      },
      navbar: {
        hidden: false,
      },
      footer: {
        hidden: false,
      },
      scrollTop: false,
      enableLocalStorage: true,
    };
  }
}
