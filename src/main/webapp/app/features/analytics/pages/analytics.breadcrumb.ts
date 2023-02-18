import { ContentHeader } from '../../../layouts/content-header/content-header.component';

export const analyticsBreadcrumb: ContentHeader = {
  headerTitle: 'Analytics',
  breadcrumb: {
    type: '',
    links: [
      {
        name: 'Home',
        isLink: true,
        link: '/',
      },
      {
        name: 'Analytics',
        isLink: false,
      },
    ],
  },
};
