import Layout from "@/layout";
import store from "@/store";
import { isRelogin } from "@/utlis/request";
import { getToken } from "@/utlis/token";
import { Message } from "element-ui";
import NProgress from "nprogress";
import Vue from "vue";
import VueRouter from "vue-router";
Vue.use(VueRouter);

// 解决两次访问相同路由地址报错
const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch((err) => err);
};

const routes = [
  {
    path: "/login",
    name: "登录",
    component: () => import("@/views/login/Login.vue"),
    hidden: true,
  },
  {
    path: "/",
    component: Layout,
    children: [
      {
        path: "/",
        name: "首页",
        icon: "el-icon-s-home",
        component: () => import("@/views/home/Home.vue"),
      },
    ],
  },
  {
    path: "/user-submenu",
    name: "用户管理",
    icon: "el-icon-user-solid",
    component: Layout,
    redirect: "/user",
    children: [
      {
        path: "/user",
        name: "用户列表",
        icon: "el-icon-s-custom",
        component: () => import("@/views/user/User.vue"),
      },
      {
        path: "/online",
        name: "在线用户",
        icon: "el-icon-s-platform",
        component: () => import("@/views/user/Online.vue"),
      },
    ],
  },
  {
    path: "/aricle-submenu",
    name: "文章管理",
    icon: "el-icon-document",
    component: Layout,
    redirect: "/article-list",
    children: [
      {
        path: "/article/:articleId",
        name: "修改文章",
        icon: "el-icon-edit",
        component: () => import("@/views/article/ArticlePublish.vue"),
        hidden: true,
      },
      {
        path: "/article-list",
        name: "文章列表",
        icon: "el-icon-s-data",
        component: () => import("@/views/article/ArticleList.vue"),
      },
      {
        path: "/article",
        name: "发布文章",
        icon: "el-icon-edit",
        component: () => import("@/views/article/ArticlePublish.vue"),
      },
      {
        path: "/category",
        name: "分类管理",
        icon: "el-icon-s-grid",
        component: () => import("@/views/category/Category.vue"),
      },
      {
        path: "/tag",
        name: "标签管理",
        icon: "el-icon-s-flag",
        component: () => import("@/views/tag/Tag.vue"),
      },
    ],
  },
  {
    path: "/message-submenu",
    name: "消息管理",
    icon: "el-icon-chat-dot-round",
    component: Layout,
    redirect: "/messages",
    children: [
      {
        path: "/message",
        name: "留言管理",
        icon: "el-icon-s-order",
        component: () => import("@/views/message/Message.vue"),
      },
      {
        path: "/comment",
        name: "评论管理",
        icon: "el-icon-s-comment",
        component: () => import("@/views/comment/Comment.vue"),
      },
    ],
  },
  {
    path: "/talk-submenu",
    name: "说说管理",
    icon: "el-icon-chat-dot-square",
    component: Layout,
    redirect: "/talk",
    children: [
      {
        path: "/talks",
        name: "发布说说",
        icon: "el-icon-s-promotion",
        component: () => import("@/views/talk/Talk.vue"),
      },
      {
        path: "/talk-list",
        name: "说说列表",
        icon: "el-icon-s-marketing",
        component: () => import("@/views/talk/TalkList.vue"),
      },
      {
        path: "/talks/:talkId",
        name: "修改说说",
        component: () => import("@/views/talk/Talk.vue"),
        hidden: true,
      },
    ],
  },
  {
    path: "/tuchung-submenu",
    name: "图床管理",
    icon: "el-icon-upload",
    component: Layout,
    redirect: "/picture",
    children: [
      {
        path: "/picture",
        name: "哔哩图床",
        icon: "el-icon-picture",
        component: () => import("@/views/picture/BiliBili.vue"),
      },
      {
        path: "/picture/delete",
        name: "图片回收站",
        component: () => import("@/views/picture/Delete.vue"),
        hidden: true,
      },
    ],
  },
  {
    path: "/system-submenu",
    name: "系统管理",
    icon: "el-icon-s-tools",
    component: Layout,
    redirect: "/website",
    children: [
      {
        path: "/website",
        name: "网站管理",
        icon: "el-icon-s-opportunity",
        component: () => import("@/views/website/WebSite.vue"),
      },
      {
        path: "/friend",
        name: "友链管理",
        icon: "el-icon-link",
        component: () => import("@/views/friend/Friend.vue"),
      },
      {
        path: "/about",
        name: "关于我",
        icon: "el-icon-user",
        component: () => import("@/views/about/About.vue"),
      },
    ],
  },
  {
    path: "/log-submenu",
    name: "日志管理",
    icon: "el-icon-s-management",
    component: Layout,
    redirect: "/operation",
    children: [
      {
        path: "/operation",
        name: "操作日志",
        icon: "el-icon-s-claim",
        component: () => import("@/views/log/Operation.vue"),
      },
      {
        path: "/exception",
        name: "异常日志",
        icon: "el-icon-s-release",
        component: () => import("@/views/log/Exception.vue"),
      },
    ],
  },
  {
    path: "/person",
    component: Layout,
    children: [
      {
        path: "/person",
        name: "个人中心",
        icon: "el-icon-platform-eleme",
        component: () => import("@/views/person/Person.vue"),
      },
    ],
  },
];

const router = new VueRouter({
  mode: "history",
  routes,
});

NProgress.configure({
  ease: "ease",
  speed: 500,
  showSpinner: false,
  trickleSpeed: 200,
  minimum: 0.3,
});

const whiteList = ["/login"];

router.beforeEach((to, from, next) => {
  NProgress.start();
  // 如果有token，说明用户登录过
  if (getToken()) {
    if (to.path === "/login") {
      next({ path: "/" });
    } else {
      if (store.getters.userId == null) {
        isRelogin.show = true;
        store
          .dispatch("user/getUserInfo")
          .then(() => {
            isRelogin.show = false;
            next();
          })
          .catch(() => {
            store.dispatch("user/LogOut").then(() => {
              next({ path: "/login" });
            });
          });
      } else {
        next();
      }
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next();
    } else {
      Message.error("请先登录");
      next({ path: "/login" });
    }
  }
  // next();
});
router.afterEach(() => {
  NProgress.done();
});

export default router;
