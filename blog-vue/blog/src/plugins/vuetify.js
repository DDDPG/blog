import Vue from "vue";
import Vuetify, {
  VBtn,
  VCard,
  VDialog,
  VIcon,
  VPagination,
  VSnackbar,
  VTextField,
} from "vuetify/lib";
import { Ripple } from "vuetify/lib/directives";

Vue.use(Vuetify, {
  components: {
    VDialog,
    VCard,
    VIcon,
    VTextField,
    VBtn,
    VSnackbar,
    VPagination,
  },
  directives: {
    Ripple,
  },
});

const opts = {
  themes: {
    light: {
      primary: "#3f51b5",
      secondary: "#b0bec5",
      accent: "#8c9eff",
      error: "#b71c1c",
    },
  },
};

export default new Vuetify(opts);
