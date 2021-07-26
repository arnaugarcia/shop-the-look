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
            hidden: boolean
        }
        scrollTop: boolean;
        enableLocalStorage: boolean;
    };
}
