import { Storage, TranslatorContext } from 'react-jhipster';

import { setLocale } from 'app/shared/reducers/locale';

TranslatorContext.setDefaultLocale('de');
TranslatorContext.setRenderInnerTextForMissingKeys(false);

export const languages: any = {
  de: { name: 'Deutsch' },
  en: { name: 'English' },
  sv: { name: 'Svenska' },
  ru: { name: 'Русский' },
  // jhipster-needle-i18n-language-key-pipe - JHipster will add/remove languages in this object
};

export const locales = Object.keys(languages).sort();

export const registerLocale = store => {
  store.dispatch(setLocale(Storage.session.get('locale', 'de')));
};
