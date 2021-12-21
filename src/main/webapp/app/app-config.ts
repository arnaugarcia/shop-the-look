import { CoreConfig } from '@core/types';

/**
 * Default App Config
 *
 * ? TIP:
 *
 * Change app config based on your preferences.
 * You can also change them on each component basis. i.e `app/main/pages/authentication/auth-login-v1/auth-login-v1.component.ts`
 *
 */

// prettier-ignore
export const coreConfig: CoreConfig = {
    layout: {
        menu: {
            hidden: false,           // Boolean: true, false
            collapsed: false,           // Boolean: true, false
        },
        // ? For horizontal menu, navbar type will work for navMenu type
        navbar: {
            hidden: false           // Boolean: true, false
        },
        footer: {
            hidden: false
        },
        scrollTop: false,                       // Boolean: true, false (Enable scroll to top button)
        enableLocalStorage: true
    }
}
