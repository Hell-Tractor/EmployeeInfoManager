<script setup lang="ts">
import { computed, onMounted } from "vue";
import { useAlertStore, useAppStore, useUserStore } from "./utils/store.ts";
import DefaultPage from "./components/DefaultPage.vue";
import LoginPage from "./components/LoginPage.vue";
import DepartTable from "./components/DepartTable.vue";
import RiskTagTable from "./components/RiskTagTable.vue";
import StaffPage from "./components/StaffPage.vue";
import UserPage from "./components/UserPage.vue";
import ViolationQueryPage from "./components/ViolationQueryPage.vue";
import EmploymentTable from "./components/EmploymentTable.vue";

interface Page {
  name: string;
  text: string;
  icon: string;
}

const pages: Page[] = [
  { name: 'home', text: '主页', icon: 'mdi-home' },
  { name: 'employment', text: '雇佣记录', icon: 'mdi-account-tie' },
  { name: 'violationQuery', text: '违规查询', icon: 'mdi-magnify' },
  { name: 'staff', text: '员工管理', icon: 'mdi-account-group' },
  { name: 'depart', text: '公司列表', icon: 'mdi-office-building' },
  { name: 'tag', text: '风险标签', icon: 'mdi-tag' },
  { name: 'user', text: '系统用户', icon: 'mdi-account' },
]

const LOGIN_PAGE: Page = { name: 'login', text: '登录', icon: 'mdi-login' };

const alertStore = useAlertStore();
const appStore = useAppStore();
const page = computed(() => appStore.page == 'login' ? LOGIN_PAGE : pages.find(p => p.name === appStore.page));
const currentUser = computed(() => useUserStore().username);

onMounted(() => {
  let token = localStorage.getItem('token');
  let username = localStorage.getItem('username');
  let level = localStorage.getItem('level');
  let departId = localStorage.getItem('departId');
  if (token && username && level && departId) {
    useUserStore().set(token, username, level, Number.parseInt(departId));
  }
})
</script>

<template>
  <v-layout class="rounded rounded-md">
    <v-navigation-drawer image="https://cdn.vuetifyjs.com/images/backgrounds/bg-2.jpg" theme="dark">
      <v-list-item title="员工管理系统" @click="appStore.redirectTo('home')"></v-list-item>
      <v-divider></v-divider>
      <v-list-item v-for="item in pages" :key = "item.name" @click="appStore.redirectTo(item.name)" :title="item.text" :prepend-icon="item.icon"></v-list-item>
      <v-divider></v-divider>
      <v-list-item :title="LOGIN_PAGE.text" @click="appStore.redirectTo('login')" :prepend-icon="LOGIN_PAGE.icon"></v-list-item>
    </v-navigation-drawer>

    <v-app-bar :title="page?.text">
      <v-spacer></v-spacer>
      <div v-if="!!currentUser" class="margin-right">欢迎您，{{ currentUser }}</div>
      <div v-else class="margin-right">未登录</div>
    </v-app-bar>

    <v-main class="d-fex align-center justify-center" style="min-height: 300px;">
      <v-alert class="align-center h-center" style="max-width: 40%;" v-if="alertStore.show" :text="alertStore.message" :color="alertStore.type" :icon="`$${alertStore.type}`"></v-alert>
      <DefaultPage v-if="page?.name === 'home'" />
      <EmploymentTable v-else-if="page?.name === 'employment'" />
      <DepartTable v-else-if="page?.name === 'depart'" />
      <ViolationQueryPage v-else-if="page?.name === 'violationQuery'" />
      <StaffPage v-else-if="page?.name === 'staff'" />
      <RiskTagTable v-else-if="page?.name === 'tag'" />
      <UserPage v-else-if="page?.name === 'user'" />
      <LoginPage v-else-if="page?.name === 'login'"/>
      <h1 v-else>Hoops! it seems like you shouldn't be here!</h1>
    </v-main>
  </v-layout>
</template>

<style scoped>
.margin-right {
  margin-right: 10px;
}
</style>
