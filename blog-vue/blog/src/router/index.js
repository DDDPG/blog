import store from "@/store";
import { getToken } from "@/utils/token";
import NProgress from "nprogress";
import Vue from "vue";
import VueRouter from "vue-router";
Vue.use(VueRouter);

// 解决两次访问相同路由地址报错
const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch((err) => err);
};

NProgress.configure({
  ease: "ease",
  speed: 500,
  showSpinner: false,
  trickleSpeed: 200,
  minimum: 0.3,
});

const routes = [
  {
    path: "/",
    component: () => import("../views/home/Home.vue"),
    meta: {
      title: "Hello World",
    },
  },
  {
    path: "/articles/:id",
    component: () => import("../views/article/Article.vue"),
    meta: {
      title: "文章详情",
    },
  },
  {
    path: "/categories/:categoryId",
    component: () => import("../views/article/ArticleList.vue"),
    meta: {
      title: "分类",
    },
  },
  {
    path: "/tags/:tagId",
    component: () => import("../views/article/ArticleList.vue"),
    meta: {
      title: "标签",
    },
  },
  {
    path: "/archives",
    component: () => import("../views/archives/Archives.vue"),
    meta: {
      title: "归档",
    },
  },
  {
    path: "/categories",
    component: () => import("../views/category/Category.vue"),
    meta: {
      title: "分类",
    },
  },
  {
    path: "/tags",
    component: () => import("../views/tag/Tag.vue"),
    meta: {
      title: "标签",
    },
  },
  {
    path: "/friends",
    component: () => import("../views/friend/Friend.vue"),
    meta: {
      title: "友情链接",
    },
  },
  {
    path: "/talks",
    component: () => import("../views/talk/TalkList.vue"),
    meta: {
      title: "说说",
    },
  },
  {
    path: "/talks/:talkId",
    component: () => import("../views/talk/Talk.vue"),
    meta: {
      title: "说说",
    },
  },
  {
    path: "/message",
    component: () => import("../views/message/Message.vue"),
    meta: {
      title: "留言板",
    },
  },
  {
    path: "/about",
    component: () => import("../views/about/About.vue"),
    meta: {
      title: "关于",
    },
  },
  {
    path: "/user",
    component: () => import("../views/user/User.vue"),
    meta: {
      title: "个人中心",
    },
  },
  {
    path: "/oauth/login/weibo",
    component: () => import("../components/OauthLogin.vue"),
  },
  {
    path: "/oauth/login/gitee",
    component: () => import("../components/OauthLogin.vue"),
  },
  {
    path: "/oauth/login/github",
    component: () => import("../components/OauthLogin.vue"),
  },
  {
    path: "/oauth/login/qq",
    component: () => import("../components/OauthLogin.vue"),
  },
  {
    path: "*",
    component: () => import("../views/error/404.vue"),
    meta: {
      title: "页面不存在",
    },
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});
router.beforeEach((to, from, next) => {
  NProgress.start();
  if (to.meta.title) {
    document.title = to.meta.title;
  }
  if (getToken()) {
    if (store.getters.userId == null) {
      store
        .dispatch("getUserInfo")
        .then(() => {
          next();
        })
        .catch(() => {
          store.dispatch("LogOut").then(() => {
            Vue.prototype.$toast({ type: "error", message: "凭证失效，请重新登录" });
            next();
          });
        });
    } else {
      next();
    }
  } else {
    next();
  }
});
router.afterEach(() => {
  window.scrollTo({
    top: 0,
    behavior: "smooth",
  });
  NProgress.done();
});

export default router;
