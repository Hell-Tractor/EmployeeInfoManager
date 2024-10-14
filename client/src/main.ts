import { createApp } from "vue";
import App from "./App.vue";

import 'vuetify/styles'
import '@mdi/font/css/materialdesignicons.css'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import { createPinia } from "pinia";

import './assets/css/common.css'
import router from "./router";

const vuetify = createVuetify({
    components,
    directives,
    icons: {
        defaultSet: 'mdi',
    }
})

const pinia = createPinia()

createApp(App).use(vuetify).use(pinia).use(router).mount('#app')