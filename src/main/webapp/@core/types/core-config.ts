// prettier-ignore

export interface CoreConfig {
  layout: {
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
